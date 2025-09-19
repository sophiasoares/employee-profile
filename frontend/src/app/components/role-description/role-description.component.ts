import { Component } from '@angular/core';
import { RoleService } from '../../services/role.service';
import { CommonModule } from '@angular/common';
import { UserRole } from '../../models/user-role.enum';

@Component({
  selector: 'app-role-description',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './role-description.component.html',
  styleUrl: './role-description.component.css'
})
export class RoleDescriptionComponent {
  protected readonly UserRole = UserRole;

  constructor(protected roleService: RoleService) {}

  formatRole(role: string): string {
    return role.replace('_', ' ').toLowerCase().replace(/\b\w/g, l => l.toUpperCase());
  }

  getRoleClass(): string {
    return this.roleService.user().role.toLowerCase().replace('_', '-');
  }

  getRoleDescription(): string {
    switch (this.roleService.user().role) {
      case UserRole.MANAGER:
        return 'You have full access to all employee data, can approve absence requests, manage team feedback, and edit all information.';
      case UserRole.CO_WORKER:
        return 'You can view non-sensitive colleague data, give and see all feedback, and request absences.';
      case UserRole.EMPLOYEE:
        return 'You can view your own profile, request absences, and see feedback that was given to you.';
      default:
        return '';
    }
  }

  getPermissionsList(): string[] {
    const permissions = this.roleService.permissions();
    const list: string[] = [];
    
    if (permissions.canViewSensitiveData) list.push('View sensitive data (salary, address, etc.)');
    if (permissions.canEditAllData) list.push('Edit all employee data');
    if (permissions.canApproveAbsences) list.push('Approve absence requests');
    if (permissions.canViewAllFeedback) list.push('View all team feedback');
    if (permissions.canGiveFeedback) list.push('Give feedback to colleagues');
    if (permissions.canViewFeedback) list.push('View feedback given to you');
    if (permissions.canViewAllEmployees) list.push('View all employee profiles');
    
    // Everyone can request absences, so we always show this
    list.push('Request time off');
    
    return list;
  }
}
