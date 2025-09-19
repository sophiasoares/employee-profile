export interface Employee {
  id: number;
  
  // Personal info
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  birthDate?: string; // ISO date string
  gender?: Gender;
  address?: string;
  
  // Employment info
  employeeId: string;
  position: string;
  department: string;
  hireDate: string; // ISO date string
  employmentStatus: EmploymentStatus;
  employmentType: EmploymentType;
  
  // Compensation (sensitive)
  salary?: number;
  currency?: string;
  
  // Manager info
  manager?: Employee;
  directReports?: Employee[];
  
  // Profile info
  bio?: string;
  skills?: string;
  profilePictureUrl?: string;
  
  // Emergency contact
  emergencyContactName?: string;
  emergencyContactPhone?: string;
  emergencyContactRelationship?: string;
  
  // System fields
  createdAt: string; 
  updatedAt: string; 
  isActive: boolean;
}

// utility funcs
export function getEmployeeFullName(employee: Employee): string {
  return `${employee.firstName} ${employee.lastName}`;
}

export function isEmployeeManager(employee: Employee): boolean {
  return employee.directReports != null && employee.directReports.length > 0;
}

export enum Gender {
  MALE = 'MALE',
  FEMALE = 'FEMALE',
  OTHER = 'OTHER',
  PREFER_NOT_TO_SAY = 'PREFER_NOT_TO_SAY'
}

export enum EmploymentStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  TERMINATED = 'TERMINATED',
  ON_LEAVE = 'ON_LEAVE',
  SUSPENDED = 'SUSPENDED',
  RETIRED = 'RETIRED'
}

export enum EmploymentType {
  FULL_TIME = 'FULL_TIME',
  PART_TIME = 'PART_TIME',
  CONTRACT = 'CONTRACT',
  INTERN = 'INTERN',
  TEMPORARY = 'TEMPORARY'
}
