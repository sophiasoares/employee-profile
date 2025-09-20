import { Injectable, signal, computed } from '@angular/core';
import { EmployeeRole, RolePermissions } from '../models/employee-role.enum';
import { Employee, EmploymentType } from '../models/employee.model';

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  // current user as Employee with role. starts with manager for demo
  private currentUser = signal<Employee>({
    id: 1,
    firstName: 'Sarah',
    lastName: 'Johnson',
    email: 'sarah.johnson@company.com',
    employeeId: 'EMP001',
    position: 'CEO',
    department: 'Executive',
    employmentType: EmploymentType.FULL_TIME,
    role: EmployeeRole.MANAGER
  });

  // permissions based on current role
  permissions = computed<RolePermissions>(() => {
    const role = this.currentUser().role;
    
    switch (role) {
      case EmployeeRole.MANAGER:
        return {
          canViewAllEmployees: true,
          canApproveAbsences: true,
          canViewAllFeedback: true,
          canGiveFeedback: true,
          canViewFeedback: true,
          canEditAllData: true,
          canViewSensitiveData: true
        };
      
      case EmployeeRole.CO_WORKER:
        return {
          canViewAllEmployees: true,
          canApproveAbsences: false,
          canViewAllFeedback: true,
          canGiveFeedback: true,
          canViewFeedback: true,
          canEditAllData: false,
          canViewSensitiveData: false
        };
      
      case EmployeeRole.EMPLOYEE:
        return {
          canViewAllEmployees: false,
          canApproveAbsences: false,
          canViewAllFeedback: false,
          canGiveFeedback: false,
          canViewFeedback: true, // Can see feedback given to them
          canEditAllData: false,
          canViewSensitiveData: false
        };
      
      default:
        return {
          canViewAllEmployees: false,
          canApproveAbsences: false,
          canViewAllFeedback: false,
          canGiveFeedback: false,
          canViewFeedback: false,
          canEditAllData: false,
          canViewSensitiveData: false
        };
    }
  });

  // expose current user as readonly signal
  user = this.currentUser.asReadonly();

  // demo employees for role switching
  private demoEmployees: Employee[] = [
    {
      id: 1,
      firstName: 'Sarah',
      lastName: 'Johnson',
      email: 'sarah.johnson@company.com',
      employeeId: 'EMP001',
      position: 'CEO',
      department: 'Executive',
      employmentType: EmploymentType.FULL_TIME,
      role: EmployeeRole.MANAGER
    },
    {
      id: 3,
      firstName: 'Emily',
      lastName: 'Rodriguez',
      email: 'emily.rodriguez@company.com',
      employeeId: 'EMP002',
      position: 'Team Lead',
      department: 'Marketing',
      employmentType: EmploymentType.FULL_TIME,
      role: EmployeeRole.CO_WORKER
    },
    {
      id: 4,
      firstName: 'James',
      lastName: 'Wilson',
      email: 'james.wilson@company.com',
      employeeId: 'EMP003',
      position: 'Software Engineer',
      department: 'IT',
      employmentType: EmploymentType.FULL_TIME,
      role: EmployeeRole.EMPLOYEE
    }
  ];

  getDemoEmployees(): Employee[] {
    return this.demoEmployees;
  }

  switchRole(employee: Employee): void {
    this.currentUser.set(employee);
  }

  getCurrentRole(): EmployeeRole {
    return this.currentUser().role;
  }

  hasPermission(permission: keyof RolePermissions): boolean {
    return this.permissions()[permission];
  }
}
