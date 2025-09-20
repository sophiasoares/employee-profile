import { Component, Input, Output, EventEmitter, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Employee, EmploymentType } from '../../models/employee.model';
import { EmployeeDataService } from '../../services/employee-data.service';

@Component({
  selector: 'app-employee-edit-dialog',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './employee-edit-dialog.component.html',
  styleUrls: ['./employee-edit-dialog.component.css']
})
export class EmployeeEditDialogComponent implements OnInit, OnChanges {
  @Input() employee: Employee | null = null;
  @Input() isOpen = false;
  @Output() closeDialog = new EventEmitter<void>();
  @Output() employeeUpdated = new EventEmitter<Employee>();

  editForm!: FormGroup;
  isSubmitting = false;
  errorMessage = '';
  
  employmentTypes = Object.values(EmploymentType);

  constructor(
    private fb: FormBuilder,
    private employeeDataService: EmployeeDataService
  ) {
    this.initializeForm();
  }

  ngOnInit(): void {
    if (this.employee) {
      this.populateForm();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['employee'] && this.employee) {
      this.populateForm();
      this.errorMessage = '';
    }
  }

  private initializeForm(): void {
    this.editForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.pattern(/^\+?[1-9]\d{1,14}$/)]],
      address: ['', [Validators.maxLength(200)]],
      employeeId: ['', [Validators.required, Validators.maxLength(20)]],
      position: ['', [Validators.required, Validators.maxLength(100)]],
      department: ['', [Validators.required, Validators.maxLength(50)]],
      hireDate: ['', [Validators.required]],
      employmentType: [EmploymentType.FULL_TIME, [Validators.required]],
      salary: ['', [Validators.min(0)]],
      bio: ['', [Validators.maxLength(1000)]],
      skills: ['', [Validators.maxLength(500)]],
      profilePictureUrl: ['', [Validators.maxLength(255)]]
    });
  }

  private populateForm(): void {
    if (this.employee) {
      // Format hire date for date input (YYYY-MM-DD)
      let formattedHireDate = '';
      if (this.employee.hireDate) {
        const date = new Date(this.employee.hireDate);
        if (!isNaN(date.getTime())) {
          formattedHireDate = date.toISOString().split('T')[0];
        }
      }

      this.editForm.patchValue({
        firstName: this.employee.firstName || '',
        lastName: this.employee.lastName || '',
        email: this.employee.email || '',
        phoneNumber: this.employee.phoneNumber || '',
        address: this.employee.address || '',
        employeeId: this.employee.employeeId || '',
        position: this.employee.position || '',
        department: this.employee.department || '',
        hireDate: formattedHireDate,
        employmentType: this.employee.employmentType || EmploymentType.FULL_TIME,
        salary: this.employee.salary || '',
        bio: this.employee.bio || '',
        skills: this.employee.skills || '',
        profilePictureUrl: this.employee.profilePictureUrl || ''
      });
    }
  }

  onSubmit(): void {
    if (this.editForm.valid && this.employee) {
      this.isSubmitting = true;
      this.errorMessage = '';

      const formValue = this.editForm.value;
      const updatedEmployee: Partial<Employee> = {
        ...formValue,
        salary: formValue.salary ? Number(formValue.salary) : undefined,
        phoneNumber: formValue.phoneNumber || undefined,
        address: formValue.address || undefined,
        bio: formValue.bio || undefined,
        skills: formValue.skills || undefined,
        profilePictureUrl: formValue.profilePictureUrl || undefined
      };

      this.employeeDataService.updateEmployee(this.employee.id, updatedEmployee)
        .subscribe({
          next: (updated) => {
            this.employeeUpdated.emit(updated);
            this.onClose();
          },
          error: (error) => {
            this.errorMessage = 'Failed to update employee. Please try again.';
            console.error('Error updating employee:', error);
          },
          complete: () => {
            this.isSubmitting = false;
          }
        });
    }
  }

  onClose(): void {
    this.editForm.reset();
    this.errorMessage = '';
    this.isSubmitting = false;
    this.closeDialog.emit();
  }

  getFieldError(fieldName: string): string {
    const field = this.editForm.get(fieldName);
    if (field?.errors && field.touched) {
      if (field.errors['required']) return `${this.getFieldLabel(fieldName)} is required`;
      if (field.errors['email']) return 'Please enter a valid email address';
      if (field.errors['pattern']) return 'Please enter a valid phone number';
      if (field.errors['maxlength']) return `${this.getFieldLabel(fieldName)} is too long`;
      if (field.errors['min']) return 'Salary must be positive';
    }
    return '';
  }

  private getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      firstName: 'First Name',
      lastName: 'Last Name',
      email: 'Email',
      phoneNumber: 'Phone Number',
      address: 'Address',
      employeeId: 'Employee ID',
      position: 'Position',
      department: 'Department',
      hireDate: 'Hire Date',
      employmentType: 'Employment Type',
      salary: 'Salary',
      bio: 'Bio',
      skills: 'Skills',
      profilePictureUrl: 'Profile Picture URL'
    };
    return labels[fieldName] || fieldName;
  }

  formatEmploymentType(type: string): string {
    return type.replace('_', ' ').toLowerCase().replace(/\b\w/g, l => l.toUpperCase());
  }
}
