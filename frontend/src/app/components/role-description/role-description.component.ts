import { Component } from '@angular/core';
import { RoleService } from '../../services/role.service';
import { CommonModule } from '@angular/common';
import { EmployeeRole } from '../../models/employee-role.enum';

@Component({
  selector: 'app-role-description',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './role-description.component.html',
  styleUrl: './role-description.component.css'
})
export class RoleDescriptionComponent {
  protected readonly EmployeeRole = EmployeeRole;

  constructor(protected roleService: RoleService) {}

  formatRole(role: string): string {
    return role.replace('_', '-').toLowerCase().replace(/\b\w/g, l => l.toUpperCase());
  }

  getRoleClass(): string {
    return this.roleService.user().role.toLowerCase().replace('_', '-');
  }

  getRoleDescription(): string {
    switch (this.roleService.user().role) {
      case EmployeeRole.MANAGER:
        return 'You have full access to all employee data, can approve absence requests, manage team feedback, and edit all information.';
      case EmployeeRole.CO_WORKER:
        return 'You can view non-sensitive colleague data, give and see all feedback, and request absences.';
      case EmployeeRole.EMPLOYEE:
        return 'You can view your own profile, request absences, and see feedback that was given to you.';
      default:
        return '';
    }
  }
}
