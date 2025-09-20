export enum EmployeeRole {
  MANAGER = 'MANAGER',
  CO_WORKER = 'CO_WORKER', 
  EMPLOYEE = 'EMPLOYEE'
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
