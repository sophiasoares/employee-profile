import { Employee } from './employee.model';

export interface AbsenceRequest {
  id: number;
  
  // Employee requesting the absence
  employee: Employee;
  
  // Manager who approved/rejected the request
  approvedBy?: Employee;
  
  absenceType: AbsenceType;
  startDate: string; // ISO date string
  endDate: string; // ISO date string
  
  isHalfDay: boolean;
  halfDayPeriod?: HalfDayPeriod;
  
  reason: string;
  managerComments?: string;
  status: AbsenceStatus;
  
  // Emergency contact during absence
  emergencyContact?: string;
  emergencyContactPhone?: string;
  
  // Work delegation
  workDelegationNotes?: string;
  delegatedTo?: Employee;
  
  // Dates for tracking
  requestedAt?: string; // ISO datetime string
  approvedAt?: string; // ISO datetime string
  rejectedAt?: string; // ISO datetime string
  
  // System fields
  createdAt: string; // ISO datetime string
  updatedAt: string; // ISO datetime string
}

export enum AbsenceType {
  VACATION = 'VACATION',
  SICK_LEAVE = 'SICK_LEAVE',
  PERSONAL_LEAVE = 'PERSONAL_LEAVE',
  MATERNITY_LEAVE = 'MATERNITY_LEAVE',
  PATERNITY_LEAVE = 'PATERNITY_LEAVE',
  BEREAVEMENT_LEAVE = 'BEREAVEMENT_LEAVE',
  JURY_DUTY = 'JURY_DUTY',
  MILITARY_LEAVE = 'MILITARY_LEAVE',
  UNPAID_LEAVE = 'UNPAID_LEAVE',
  SABBATICAL = 'SABBATICAL',
  TRAINING = 'TRAINING',
  CONFERENCE = 'CONFERENCE',
  OTHER = 'OTHER'
}

export enum AbsenceStatus {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  CANCELLED = 'CANCELLED',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED'
}

export enum HalfDayPeriod {
  MORNING = 'MORNING',
  AFTERNOON = 'AFTERNOON'
}
