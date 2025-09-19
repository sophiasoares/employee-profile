import { Employee } from './employee.model';

export interface Feedback {
  id: number;
  
  // Employee receiving the feedback
  employee: Employee;
  
  // Employee giving the feedback
  feedbackGiver: Employee;
  
  content: string;
  feedbackType: FeedbackType;
  
  // AI Enhanced content
  aiEnhancedContent?: string;
  isAiEnhanced: boolean;
}

export enum FeedbackType {
  POSITIVE = 'POSITIVE',
  CONSTRUCTIVE = 'CONSTRUCTIVE',
  GENERAL = 'GENERAL',
  PERFORMANCE = 'PERFORMANCE',
  TEAMWORK = 'TEAMWORK',
  COMMUNICATION = 'COMMUNICATION',
  CREATIVITY = 'CREATIVITY',
  LEARNING = 'LEARNING',
  LEADERSHIP = 'LEADERSHIP',
  MANAGER_FEEDBACK = 'MANAGER_FEEDBACK',
  PERFORMANCE_REVIEW = 'PERFORMANCE_REVIEW'
}
