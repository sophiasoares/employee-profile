import { Component, OnInit, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmployeeDataService } from '../../services/employee-data.service';
import { RoleService } from '../../services/role.service';
import { Employee, getFullName, EmploymentType } from '../../models/employee.model';
import { EmployeeFeedbackDialogComponent } from '../employee-feedback-dialog/employee-feedback-dialog.component';
import { EmployeeAbsenceDialogComponent } from '../employee-absence-dialog/employee-absence-dialog.component';

@Component({
  selector: 'app-employee-profile',
  standalone: true,
  imports: [CommonModule, EmployeeFeedbackDialogComponent, EmployeeAbsenceDialogComponent],
  templateUrl: './employee-profile.component.html',
  styleUrls: ['./employee-profile.component.css']
})
export class EmployeeProfileComponent implements OnInit {
  
  currentEmployee: Employee | null = null;
  
  // Dialog states
  isFeedbackDialogOpen = false;
  isAbsenceDialogOpen = false;

  constructor(
    protected employeeDataService: EmployeeDataService,
    protected roleService: RoleService
  ) {
    // Watch for changes in employee data
    effect(() => {
      const employees = this.employeeDataService.filteredEmployees();
      if (employees.length > 0) {
        this.loadCurrentEmployee();
      }
    });
  }

  ngOnInit(): void {
    this.employeeDataService.loadAllData();
  }

  private loadCurrentEmployee(): void {
    const currentUser = this.roleService.user();
    const employees = this.employeeDataService.filteredEmployees();
    
    // Find the current user in the employee list
    this.currentEmployee = employees.find(employee => employee.id === currentUser.id) || null;
  }

  getFullName(employee: Employee): string {
    return getFullName(employee);
  }

  formatDate(dateString: string | undefined): string {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString();
  }

  formatEmploymentType(type: EmploymentType): string {
    return type.replace('_', ' ').toLowerCase().replace(/\b\w/g, l => l.toUpperCase());
  }

  getTypeClass(type: EmploymentType): string {
    switch (type) {
      case EmploymentType.FULL_TIME: return 'type-full-time';
      case EmploymentType.PART_TIME: return 'type-part-time';
      case EmploymentType.CONTRACT: return 'type-contract';
      case EmploymentType.INTERN: return 'type-intern';
      case EmploymentType.TEMPORARY: return 'type-temporary';
      default: return 'type-default';
    }
  }

  formatSalary(salary: number | undefined): string {
    if (!salary) return 'N/A';
    return new Intl.NumberFormat('de-DE', {
      style: 'decimal'
    }).format(salary);
  }

  // Dialog methods
  openFeedbackDialog(): void {
    this.isFeedbackDialogOpen = true;
  }

  closeFeedbackDialog(): void {
    this.isFeedbackDialogOpen = false;
  }

  openAbsenceDialog(): void {
    this.isAbsenceDialogOpen = true;
  }

  closeAbsenceDialog(): void {
    this.isAbsenceDialogOpen = false;
  }
}
