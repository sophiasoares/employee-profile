import { Component, signal } from '@angular/core';
import { HeaderComponent } from './components/header/header.component';
import { RoleDescriptionComponent } from './components/role-description/role-description.component';
import { EmployeeListComponent } from './components/employee-list/employee-list.component';
import { EmployeeProfileComponent } from './components/employee-profile/employee-profile.component';
import { RoleService } from './services/role.service';

@Component({
  selector: 'app-root',
  imports: [HeaderComponent, RoleDescriptionComponent, EmployeeListComponent, EmployeeProfileComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('employee-profile');

  constructor(protected roleService: RoleService) {}

  formatRole(role: string): string {
    return role.replace('_', ' ').toLowerCase().replace(/\b\w/g, l => l.toUpperCase());
  }

  getRoleDescription(): string {
    switch (this.roleService.user().role) {
      case 'MANAGER':
        return 'You have full access to employee data, can approve absence requests, and manage team feedback.';
      case 'CO_WORKER':
        return 'You can view colleague profiles (excluding sensitive data), give feedback, and see public feedback.';
      case 'EMPLOYEE':
        return 'You can view your own profile, request absences, and access limited colleague information.';
      default:
        return '';
    }
  }

  // checks if the role is 'EMPLOYEE' so that the right components are displayed
  protected isEmployee(): boolean {
    return this.roleService.user().role === 'EMPLOYEE';
  }
}
