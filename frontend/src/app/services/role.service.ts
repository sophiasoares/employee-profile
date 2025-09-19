import { Injectable, signal, computed } from '@angular/core';
import { UserRole, User, RolePermissions } from '../models/user-role.enum';

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  // current user. starts with manager for demo
  private currentUser = signal<User>({
    id: 1,
    name: 'Sarah Johnson',
    role: UserRole.MANAGER,
    email: 'sarah.johnson@company.com'
  });

  // permissions based on current role
  permissions = computed<RolePermissions>(() => {
    const role = this.currentUser().role;
    
    switch (role) {
      case UserRole.MANAGER:
        return {
          canViewAllEmployees: true,
          canApproveAbsences: true,
          canViewAllFeedback: true,
          canGiveFeedback: true,
          canViewFeedback: true,
          canEditAllData: true,
          canViewSensitiveData: true
        };
      
      case UserRole.CO_WORKER:
        return {
          canViewAllEmployees: true,
          canApproveAbsences: false,
          canViewAllFeedback: true,
          canGiveFeedback: true,
          canViewFeedback: true,
          canEditAllData: false,
          canViewSensitiveData: false
        };
      
      case UserRole.EMPLOYEE:
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

  // demo users for role switching
  private demoUsers: User[] = [
    {
      id: 1,
      name: 'Sarah Johnson',
      role: UserRole.MANAGER,
      email: 'sarah.johnson@company.com'
    },
    {
      id: 2,
      name: 'Emily Rodriguez',
      role: UserRole.CO_WORKER,
      email: 'emily.rodriguez@company.com'
    },
    {
      id: 3,
      name: 'James Wilson',
      role: UserRole.EMPLOYEE,
      email: 'james.wilson@company.com'
    }
  ];

  getDemoUsers(): User[] {
    return this.demoUsers;
  }

  switchRole(user: User): void {
    this.currentUser.set(user);
  }

  getCurrentRole(): UserRole {
    return this.currentUser().role;
  }

  hasPermission(permission: keyof RolePermissions): boolean {
    return this.permissions()[permission];
  }
}
