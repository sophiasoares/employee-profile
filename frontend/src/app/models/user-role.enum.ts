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
  canViewAllEmployees: boolean;
  canApproveAbsences: boolean;
  canViewAllFeedback: boolean;
  canGiveFeedback: boolean;
  canViewFeedback: boolean;
  canEditAllData: boolean;
  canViewSensitiveData: boolean;
}
