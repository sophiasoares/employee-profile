import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Employee } from '../../models/employee.model';
import { AbsenceRequest, AbsenceType } from '../../models/absence-request.model';
import { EmployeeService } from '../../services/employee.service';
import { RoleService } from '../../services/role.service';

@Component({
  selector: 'app-employee-absence-dialog',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './employee-absence-dialog.component.html',
  styleUrls: ['./employee-absence-dialog.component.css']
})
export class EmployeeAbsenceDialogComponent implements OnChanges {
  @Input() employee: Employee | null = null;
  @Input() isOpen = false;
  @Output() closeDialog = new EventEmitter<void>();

  absenceRequests: AbsenceRequest[] = [];
  isLoading = false;
  errorMessage = '';
  
  absenceForm!: FormGroup;
  isSubmitting = false;
  submitErrorMessage = '';
  
  absenceTypes = Object.values(AbsenceType);

  constructor(
    private employeeService: EmployeeService,
    private roleService: RoleService,
    private fb: FormBuilder
  ) {
    this.initializeForm();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['employee'] && this.employee) {
      this.initializeForm();
    }
    
    if (changes['isOpen'] && this.isOpen && this.employee) {
      this.loadAbsenceRequests();
    }
  }

  private initializeForm(): void {
    this.absenceForm = this.fb.group({
      absenceType: [AbsenceType.VACATION, [Validators.required]],
      startDate: ['', [Validators.required]],
      endDate: ['', [Validators.required]],
      reason: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(1000)]]
    });
  }

  private loadAbsenceRequests(): void {
    if (!this.employee) {
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.absenceRequests = [];

    this.employeeService.getAbsenceRequestsByEmployee(this.employee.id).subscribe({
      next: (requests) => {
        this.absenceRequests = requests || [];
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Failed to load absence requests:', error);
        this.errorMessage = `Failed to load absence requests: ${error.status} ${error.statusText}`;
        this.isLoading = false;
      }
    });
  }

  onSubmitAbsenceRequest(): void {
    if (this.absenceForm.valid && this.employee) {
      this.isSubmitting = true;
      this.submitErrorMessage = '';

      const formValue = this.absenceForm.value;
      
      // Create absence request object that matches backend expectations
      const currentUser = this.roleService.user();
      
      const newRequest = {
        employee: currentUser,
        absenceType: formValue.absenceType,
        startDate: formValue.startDate,
        endDate: formValue.endDate,
        reason: formValue.reason
      };

      this.employeeService.createAbsenceRequest(newRequest).subscribe({
        next: (_) => {
          // Reload absence requests to show the new entry
          this.loadAbsenceRequests();
          
          // Reset form properly
          this.absenceForm.reset();
          this.absenceForm.patchValue({ 
            absenceType: AbsenceType.VACATION,
            startDate: '',
            endDate: '',
            reason: ''
          });
          
          this.isSubmitting = false;
        },
        error: (error) => {
          console.error('Failed to submit absence request:', error);
          this.submitErrorMessage = 'Failed to submit absence request. Please try again.';
          this.isSubmitting = false;
        }
      });
    } else {
      this.submitErrorMessage = 'Please fill out the form correctly.';
    }
  }

  onClose(): void {
    this.absenceForm.reset();
    this.submitErrorMessage = '';
    this.closeDialog.emit();
  }

  getFullName(employee: Employee): string {
    return `${employee.firstName} ${employee.lastName}`;
  }

  formatAbsenceType(type: string): string {
    return type.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, l => l.toUpperCase());
  }

  getAbsenceTypeClass(type: string): string {
    switch (type) {
      case 'VACATION': return 'absence-vacation';
      case 'SICK_LEAVE': return 'absence-sick';
      case 'PERSONAL_LEAVE': return 'absence-personal';
      case 'MATERNITY_LEAVE': return 'absence-maternity';
      case 'PATERNITY_LEAVE': return 'absence-paternity';
      case 'BEREAVEMENT_LEAVE': return 'absence-bereavement';
      case 'JURY_DUTY': return 'absence-jury';
      case 'MILITARY_LEAVE': return 'absence-military';
      case 'UNPAID_LEAVE': return 'absence-unpaid';
      case 'SABBATICAL': return 'absence-sabbatical';
      case 'TRAINING': return 'absence-training';
      case 'CONFERENCE': return 'absence-conference';
      case 'REMOTE_WORK': return 'absence-remote';
      case 'OTHER': return 'absence-other';
      default: return 'absence-default';
    }
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleDateString();
  }

  calculateDuration(startDate: string, endDate: string): number {
    const start = new Date(startDate);
    const end = new Date(endDate);
    const diffTime = Math.abs(end.getTime() - start.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
    return diffDays;
  }

  getFieldError(fieldName: string): string {
    const field = this.absenceForm.get(fieldName);
    if (field?.errors && field.touched) {
      if (field.errors['required']) return `${this.getFieldLabel(fieldName)} is required`;
      if (field.errors['minlength']) return `${this.getFieldLabel(fieldName)} must be at least 10 characters`;
      if (field.errors['maxlength']) return `${this.getFieldLabel(fieldName)} must not exceed 1000 characters`;
    }
    return '';
  }

  getTodayDateString(): string {
    return new Date().toISOString().split('T')[0];
  }

  // check if the current user is viewing their own profile
  // i can only request absence for myself
  isViewingOwnProfile(): boolean {
    if (!this.employee) return false;
    const currentUser = this.roleService.user();
    return currentUser.id === this.employee.id;
  }

  private getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      absenceType: 'Absence type',
      startDate: 'Start date',
      endDate: 'End date',
      reason: 'Reason'
    };
    return labels[fieldName] || fieldName;
  }
}
