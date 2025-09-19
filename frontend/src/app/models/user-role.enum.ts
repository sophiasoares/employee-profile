export enum UserRole {
  MANAGER = 'MANAGER',
  CO_WORKER = 'CO_WORKER', 
  EMPLOYEE = 'EMPLOYEE'
}

export interface User {
  id: number;
  name: string;
  role: UserRole;
  email: string;
}

export interface RolePermissions {
  canViewSalary: boolean;
  canViewAllEmployees: boolean;
  canApproveAbsences: boolean;
  canViewAllFeedback: boolean;
  canManageEmployees: boolean;
}
