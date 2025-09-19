import { Injectable, computed, signal } from '@angular/core';
import { Observable, map, catchError, of } from 'rxjs';
import { EmployeeService } from './employee.service';
import { RoleService } from './role.service';
import { Employee } from '../models/employee.model';
import { Feedback } from '../models/feedback.model';
import { AbsenceRequest } from '../models/absence-request.model';

@Injectable({
  providedIn: 'root'
})
export class EmployeeDataService {
  private employees = signal<Employee[]>([]);
  private feedback = signal<Feedback[]>([]);
  private absenceRequests = signal<AbsenceRequest[]>([]);
  private loading = signal<boolean>(false);
  private error = signal<string | null>(null);

  // filtered data based on current user permissions
  filteredEmployees = computed(() => {
    const allEmployees = this.employees();
    const currentUser = this.roleService.user();
    const permissions = this.roleService.permissions();

    if (permissions.canViewAllEmployees) {
      // Manager and co-worker can see all employees
      return allEmployees.map(emp => this.filterEmployeeData(emp));
    } else {
      // Employee can only see their own data
      return allEmployees
        .filter(emp => emp.id === currentUser.id)
        .map(emp => this.filterEmployeeData(emp));
    }
  });

  filteredFeedback = computed(() => {
    const allFeedback = this.feedback();
    const currentUser = this.roleService.user();
    const permissions = this.roleService.permissions();

    if (permissions.canViewAllFeedback) {
      // Manager and co-worker can see all feedback
      return allFeedback;
    } else {
      // Employee can only see feedback given to them
      return allFeedback.filter(fb => fb.employee.id === currentUser.id);
    }
  });

  filteredAbsenceRequests = computed(() => {
    const allRequests = this.absenceRequests();
    const currentUser = this.roleService.user();
    const permissions = this.roleService.permissions();

    if (permissions.canApproveAbsences) {
      // Manager can see all requests
      return allRequests;
    } else {
      // Employee and co-worker can only see their own requests
      return allRequests.filter(req => req.employee.id === currentUser.id);
    }
  });

  readonly isLoading = this.loading.asReadonly();
  readonly errorMessage = this.error.asReadonly();

  constructor(
    private employeeService: EmployeeService,
    private roleService: RoleService
  ) {}

  loadAllData(): void {
    this.loading.set(true);
    this.error.set(null);

    // Load employees
    this.employeeService.getAllEmployees().pipe(
      catchError(err => {
        console.error('Error loading employees:', err);
        this.error.set('Failed to load employee data');
        return of([]);
      })
    ).subscribe(employees => {
      this.employees.set(employees);
    });

    // Load feedback
    this.employeeService.getAllFeedback().pipe(
      catchError(err => {
        console.error('Error loading feedback:', err);
        return of([]);
      })
    ).subscribe(feedback => {
      this.feedback.set(feedback);
    });

    // load absence requests
    this.employeeService.getAllAbsenceRequests().pipe(
      catchError(err => {
        console.error('Error loading absence requests:', err);
        return of([]);
      })
    ).subscribe(requests => {
      this.absenceRequests.set(requests);
      this.loading.set(false);
    });
  }

  // Filter employee data based on permissions
  private filterEmployeeData(employee: Employee): Employee {
    const permissions = this.roleService.permissions();
    const currentUser = this.roleService.user();
    const isOwnProfile = employee.id === currentUser.id;
    
    if (permissions.canViewSensitiveData || isOwnProfile) {
      return employee; // Managers and self can see all data
    } else {
      // Co-workers cannot see sensitive data
      return {
        ...employee,
        phoneNumber: undefined,
        hireDate: undefined,
        salary: undefined
      };
    }
  }

  // Check if the given employee ID is the current user
  isCurrentUser(employeeId: number): boolean {
    return this.roleService.user().id === employeeId;
  }

  updateEmployee(id: number, updates: Partial<Employee>): Observable<Employee> {
    const permissions = this.roleService.permissions();
    
    if (!permissions.canEditAllData) {
      throw new Error('You do not have permission to edit employee data');
    }

    return this.employeeService.updateEmployee(id, updates).pipe(
      map(updatedEmployee => {
        const currentEmployees = this.employees();
        const index = currentEmployees.findIndex(emp => emp.id === id);
        if (index !== -1) {
          const newEmployees = [...currentEmployees];
          newEmployees[index] = updatedEmployee;
          this.employees.set(newEmployees);
        }
        return updatedEmployee;
      })
    );
  }

  createFeedback(feedback: Omit<Feedback, 'id' | 'createdAt'>): Observable<Feedback> {
    const permissions = this.roleService.permissions();
    
    if (!permissions.canGiveFeedback) {
      throw new Error('You do not have permission to give feedback');
    }

    return this.employeeService.createFeedback(feedback).pipe(
      map(newFeedback => {
        const currentFeedback = this.feedback();
        this.feedback.set([...currentFeedback, newFeedback]);
        return newFeedback;
      })
    );
  }

  createAbsenceRequest(request: Omit<AbsenceRequest, 'id' | 'createdAt'>): Observable<AbsenceRequest> {
    return this.employeeService.createAbsenceRequest(request).pipe(
      map(newRequest => {
        const currentRequests = this.absenceRequests();
        this.absenceRequests.set([...currentRequests, newRequest]);
        return newRequest;
      })
    );
  }
}
