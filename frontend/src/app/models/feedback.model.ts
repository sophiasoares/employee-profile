import { Employee } from './employee.model';

export interface Feedback {
  id: number;
  
  // Employee receiving the feedback
  employee: Employee;
  
  // Employee giving the feedback (can be null for anonymous feedback)
  feedbackGiver?: Employee;
  
  title: string;
  content: string;
  feedbackType: FeedbackType;
  rating?: number; // 1-5 scale
  
  // AI Enhanced content
  aiEnhancedContent?: string;
  isAiEnhanced: boolean;
  
  // Visibility and Status
  isAnonymous: boolean;
  isPublic: boolean;
  status: FeedbackStatus;
  
  // Categories for better organization
  category?: string;
  tags?: string; // Comma-separated tags
  
  // System fields
  createdAt: string; // ISO datetime string
  updatedAt: string; // ISO datetime string
}

export enum FeedbackType {
  POSITIVE = 'POSITIVE',
  CONSTRUCTIVE = 'CONSTRUCTIVE',
  GENERAL = 'GENERAL',
  PERFORMANCE_REVIEW = 'PERFORMANCE_REVIEW',
  PEER_REVIEW = 'PEER_REVIEW',
  MANAGER_REVIEW = 'MANAGER_REVIEW'
}

export enum FeedbackStatus {
  ACTIVE = 'ACTIVE',
  ARCHIVED = 'ARCHIVED',
  HIDDEN = 'HIDDEN',
  FLAGGED = 'FLAGGED'
}
