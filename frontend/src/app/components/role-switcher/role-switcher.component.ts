import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RoleService } from '../../services/role.service';
import { EmployeeRole } from '../../models/employee-role.enum';
import { Employee, getFullName } from '../../models/employee.model';

@Component({
  selector: 'app-role-switcher',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './role-switcher.component.html',
  styleUrl: './role-switcher.component.css'
})
export class RoleSwitcherComponent {
  protected isDropdownOpen = signal(false);
  protected demoEmployees: Employee[];

  constructor(protected roleService: RoleService) {
    this.demoEmployees = roleService.getDemoEmployees();
  }

  toggleDropdown(): void {
    this.isDropdownOpen.update(current => !current);
  }

  selectUser(employee: Employee): void {
    this.roleService.switchRole(employee);
    this.isDropdownOpen.set(false);
  }

  getFullName(employee: Employee): string {
    return getFullName(employee);
  }

  formatRole(role: EmployeeRole): string {
    return role.replace('_', '-').toLowerCase().replace(/\b\w/g, l => l.toUpperCase());
  }

  getRoleClass(role: EmployeeRole): string {
    switch (role) {
      case EmployeeRole.MANAGER: return 'manager';
      case EmployeeRole.CO_WORKER: return 'co-worker';
      case EmployeeRole.EMPLOYEE: return 'employee';
      default: return 'employee';
    }
  }
}
