export interface Employee {
  id: number;
  
  // Personal Information
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  address?: string;
  
  // Employment Information
  employeeId: string;
  position: string;
  department: string;
  hireDate?: string;
  employmentType: EmploymentType;
  salary?: number;
  
  // Profile Information
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
