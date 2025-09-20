import { EmployeeRole } from './employee-role.enum';

export interface Employee {
  id: number; // for db
  
  // Personal info
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  address?: string;
  
  // Employment info
  employeeId: string; // for general use, something like EMP001
  position: string;
  department: string;
  hireDate?: string;
  employmentType: EmploymentType;
  salary?: number;
  
  // Role and permissions
  role: EmployeeRole;
  
  // Profile info
  bio?: string;
  skills?: string;
  profilePictureUrl?: string;
}

export function getFullName(employee: Employee): string {
  return `${employee.firstName} ${employee.lastName}`;
}

export enum EmploymentType {
  FULL_TIME = 'FULL_TIME',
  PART_TIME = 'PART_TIME',
  CONTRACT = 'CONTRACT',
  INTERN = 'INTERN',
  TEMPORARY = 'TEMPORARY'
}
