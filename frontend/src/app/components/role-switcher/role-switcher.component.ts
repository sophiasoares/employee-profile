import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RoleService } from '../../services/role.service';
import { User, UserRole } from '../../models/user-role.enum';

@Component({
  selector: 'app-role-switcher',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './role-switcher.component.html',
  styleUrl: './role-switcher.component.css'
})
export class RoleSwitcherComponent {
  protected isDropdownOpen = signal(false);
  protected demoUsers: User[];

  constructor(protected roleService: RoleService) {
    this.demoUsers = roleService.getDemoUsers();
  }

  toggleDropdown(): void {
    this.isDropdownOpen.update(open => !open);
  }

  selectUser(user: User): void {
    this.roleService.switchRole(user);
    this.isDropdownOpen.set(false);
  }

  formatRole(role: UserRole): string {
    return role.replace('_', '-').toLowerCase();
  }

  getRoleClass(role?: UserRole): string {
    const userRole = role || this.roleService.user().role;
    return userRole.toLowerCase().replace('_', '-');
  }
}
