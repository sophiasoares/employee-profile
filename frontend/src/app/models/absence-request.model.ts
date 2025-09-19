import { Employee } from './employee.model';

export interface AbsenceRequest {
  id: number;
  employee: Employee;   // Employee requesting the absence
  absenceType: AbsenceType;
  startDate: string; // ISO date string
  endDate: string; 
  reason: string;
  createdAt: string;
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
