import { Component, signal } from '@angular/core';
import { HeaderComponent } from './components/header/header.component';
import { RoleService } from './services/role.service';

@Component({
  selector: 'app-root',
  imports: [HeaderComponent],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
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
}
