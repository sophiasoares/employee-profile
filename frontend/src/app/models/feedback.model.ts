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
  PERFORMANCE_REVIEW = 'PERFORMANCE_REVIEW',
  PEER_FEEDBACK = 'PEER_FEEDBACK',
  MANAGER_FEEDBACK = 'MANAGER_FEEDBACK',
  SELF_ASSESSMENT = 'SELF_ASSESSMENT',
  GOAL_SETTING = 'GOAL_SETTING',
  DEVELOPMENT_FEEDBACK = 'DEVELOPMENT_FEEDBACK'
}
