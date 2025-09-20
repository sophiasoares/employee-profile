import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Employee, getFullName } from '../../models/employee.model';
import { Feedback, FeedbackType } from '../../models/feedback.model';
import { EmployeeService } from '../../services/employee.service';
import { RoleService } from '../../services/role.service';

@Component({
  selector: 'app-employee-feedback-dialog',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './employee-feedback-dialog.component.html',
  styleUrls: ['./employee-feedback-dialog.component.css']
})
export class EmployeeFeedbackDialogComponent implements OnChanges {
  @Input() employee: Employee | null = null;
  @Input() isOpen = false;
  @Output() closeDialog = new EventEmitter<void>();

  feedbackReceived: Feedback[] = [];
  feedbackGiven: Feedback[] = [];
  isLoading = false;
  errorMessage = '';

  // Form for writing new feedback
  feedbackForm!: FormGroup;
  isSubmitting = false;
  submitErrorMessage = '';
  
  // AI Enhancement
  isEnhancing = false;

  feedbackTypes = Object.values(FeedbackType);

  constructor(
    private employeeService: EmployeeService,
    private roleService: RoleService,
    private fb: FormBuilder
  ) {
    this.initializeForm();
  }

  // will get triggered when the input properties change
  // used instead of ngOnInit() because we need to load feedback when the dialog is opened
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['employee'] && this.employee) {
      this.initializeForm();
    }
    
    if (changes['isOpen'] && this.isOpen && this.employee) {
      this.loadFeedback();
    }
  }

  private initializeForm(): void {
    this.feedbackForm = this.fb.group({
      content: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(1000)]],
      feedbackType: [FeedbackType.POSITIVE, [Validators.required]]
    });
  }

  private loadFeedback(): void {
    if (!this.employee) {
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.feedbackReceived = [];
    this.feedbackGiven = [];
    
    let completedRequests = 0;
    const totalRequests = 2;

    const checkComplete = () => {
      completedRequests++;
      if (completedRequests >= totalRequests) {
        this.isLoading = false;
      }
    };

    this.employeeService.getFeedbackForEmployee(this.employee.id).subscribe({
      next: (feedback) => {
        this.feedbackReceived = feedback || [];
        checkComplete();
      },
      error: (error) => {
        this.errorMessage = `Failed to load received feedback: ${error.status} ${error.statusText}`;
        checkComplete();
      }
    });
    
    this.employeeService.getFeedbackByEmployee(this.employee.id).subscribe({
      next: (feedback) => {
        this.feedbackGiven = feedback || [];
        checkComplete();
      },
      error: (error) => {
        this.errorMessage = `Failed to load given feedback: ${error.status} ${error.statusText}`;
        checkComplete();
      }
    });
  }

  onSubmitFeedback(): void {
    if (this.feedbackForm.valid && this.employee) {
      this.isSubmitting = true;
      this.submitErrorMessage = '';

      const formValue = this.feedbackForm.value;
      
      // Create feedback object that matches backend expectations
      const currentUser = this.roleService.user();
      
      const newFeedback = {
        employee: this.employee,
        feedbackGiver: currentUser,
        content: formValue.content,
        feedbackType: formValue.feedbackType,
        isAiEnhanced: false
      };

      this.employeeService.createFeedback(newFeedback).subscribe({
        next: (_) => {
          // Reload feedback to show the new entry
          this.loadFeedback();
          
          // Reset form properly
          this.feedbackForm.reset();
          this.feedbackForm.patchValue({ 
            feedbackType: FeedbackType.POSITIVE,
            content: ''
          });
          
          this.isSubmitting = false;
        },
        error: (error) => {
          console.error('Failed to submit feedback:', error);
          this.submitErrorMessage = 'Failed to submit feedback. Please try again.';
          this.isSubmitting = false;
        }
      });
    } else {
      this.submitErrorMessage = 'Please fill out the form correctly.';
    }
  }

  enhanceWithAI(): void {
    const currentContent = this.feedbackForm.get('content')?.value;
    
    if (!currentContent || currentContent.trim().length === 0) {
      return;
    }

    this.isEnhancing = true;
    this.submitErrorMessage = '';

    this.employeeService.enhanceFeedbackText(currentContent).subscribe({
      next: (enhancedText) => {
        // Update the form with the enhanced text
        this.feedbackForm.patchValue({
          content: enhancedText
        });
        this.isEnhancing = false;
      },
      error: (error) => {
        console.error('AI enhancement failed:', error);
        this.submitErrorMessage = 'Failed to enhance text with AI. Please try again.';
        this.isEnhancing = false;
      }
    });
  }

  onClose(): void {
    this.feedbackForm.reset();
    this.submitErrorMessage = '';
    this.closeDialog.emit();
  }

  getFullName(employee: Employee): string {
    return getFullName(employee);
  }

  getFeedbackTypeClass(type: string): string {
    switch (type) {
      case 'POSITIVE': return 'feedback-positive';
      case 'CONSTRUCTIVE': return 'feedback-constructive';
      case 'PERFORMANCE_REVIEW': return 'feedback-performance';
      case 'PEER_FEEDBACK': return 'feedback-peer';
      case 'MANAGER_FEEDBACK': return 'feedback-manager';
      case 'SELF_ASSESSMENT': return 'feedback-self';
      case 'GOAL_SETTING': return 'feedback-goal';
      case 'DEVELOPMENT_FEEDBACK': return 'feedback-development';
      default: return 'feedback-default';
    }
  }

  formatFeedbackType(type: string): string {
    return type.replace('_', ' ').toLowerCase().replace(/\b\w/g, l => l.toUpperCase());
  }

  canGiveFeedback(): boolean {
    return this.roleService.permissions().canGiveFeedback;
  }

  // check if the current user is viewing their own profile so that they 
  // can't give feedback to themselves
  isViewingOwnProfile(): boolean {
    if (!this.employee) return false;
    const currentUser = this.roleService.user();
    return currentUser.id === this.employee.id;
  }

  getFieldError(fieldName: string): string {
    const field = this.feedbackForm.get(fieldName);
    if (field?.errors && field.touched) {
      if (field.errors['required']) return `${this.getFieldLabel(fieldName)} is required`;
      if (field.errors['minlength']) return `${this.getFieldLabel(fieldName)} must be at least 10 characters`;
      if (field.errors['maxlength']) return `${this.getFieldLabel(fieldName)} must not exceed 1000 characters`;
    }
    return '';
  }

  private getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      content: 'Feedback content',
      feedbackType: 'Feedback type'
    };
    return labels[fieldName] || fieldName;
  }
}
