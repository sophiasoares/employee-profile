import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmployeeDataService } from '../../services/employee-data.service';
import { RoleService } from '../../services/role.service';
import { Employee, getFullName } from '../../models/employee.model';
import { EmployeeEditDialogComponent } from '../employee-edit-dialog/employee-edit-dialog.component';

@Component({
  selector: 'app-employee-list',
  standalone: true,
  imports: [CommonModule, EmployeeEditDialogComponent],
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.css']
})
export class EmployeeListComponent implements OnInit {
  
  // for the edit dialog
  isEditDialogOpen = false;
  selectedEmployee: Employee | null = null;

  constructor(
    protected employeeDataService: EmployeeDataService,
    protected roleService: RoleService
  ) {}

  ngOnInit(): void {
    // load data when component initializes
    this.employeeDataService.loadAllData();
  }

  getFullName(employee: Employee): string {
    return getFullName(employee);
  }

  formatDate(dateString: string): string {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString();
  }

  formatEmploymentType(type: string): string {
    return type.replace('_', ' ').toLowerCase().replace(/\b\w/g, l => l.toUpperCase());
  }

  getTypeClass(type: string): string {
    switch (type) {
      case 'FULL_TIME': return 'type-full-time';
      case 'PART_TIME': return 'type-part-time';
      case 'CONTRACT': return 'type-contract';
      case 'INTERN': return 'type-intern';
      case 'TEMPORARY': return 'type-temporary';
      default: return 'type-default';
    }
  }

  formatSalary(salary: number | undefined): string {
    if (!salary) return 'N/A';
    return new Intl.NumberFormat('de-DE', {
      style: 'decimal'
    }).format(salary);
  }

  openEditDialog(employee: Employee): void {
    this.selectedEmployee = employee;
    this.isEditDialogOpen = true;
  }

  closeEditDialog(): void {
    this.isEditDialogOpen = false;
    this.selectedEmployee = null;
  }

  onEmployeeUpdated(updatedEmployee: Employee): void {
    // for the future: create a snackbar
    console.log('Employee updated successfully:', updatedEmployee);
  }
}
