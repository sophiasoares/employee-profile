import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Employee } from '../models/employee.model';
import { Feedback } from '../models/feedback.model';
import { AbsenceRequest } from '../models/absence-request.model';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private readonly apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  // Employee endpoints
  getAllEmployees(): Observable<Employee[]> {
    return this.http.get<Employee[]>(`${this.apiUrl}/employees`);
  }

  getEmployeeById(id: number): Observable<Employee> {
    return this.http.get<Employee>(`${this.apiUrl}/employees/${id}`);
  }

  updateEmployee(id: number, employee: Partial<Employee>): Observable<Employee> {
    return this.http.put<Employee>(`${this.apiUrl}/employees/${id}`, employee);
  }

  createEmployee(employee: Omit<Employee, 'id'>): Observable<Employee> {
    return this.http.post<Employee>(`${this.apiUrl}/employees`, employee);
  }

  deleteEmployee(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/employees/${id}`);
  }

  // Feedback endpoints
  getAllFeedback(): Observable<Feedback[]> {
    return this.http.get<Feedback[]>(`${this.apiUrl}/feedback`);
  }

  getFeedbackForEmployee(employeeId: number): Observable<Feedback[]> {
    return this.http.get<Feedback[]>(`${this.apiUrl}/feedback/employee/${employeeId}`);
  }

  getFeedbackByEmployee(employeeId: number): Observable<Feedback[]> {
    return this.http.get<Feedback[]>(`${this.apiUrl}/feedback/by-employee/${employeeId}`);
  }

  createFeedback(feedback: Omit<Feedback, 'id' | 'createdAt' | 'updatedAt'>): Observable<Feedback> {
    return this.http.post<Feedback>(`${this.apiUrl}/feedback`, feedback);
  }

  // Absence request endpoints
  getAllAbsenceRequests(): Observable<AbsenceRequest[]> {
    return this.http.get<AbsenceRequest[]>(`${this.apiUrl}/absence-requests`);
  }

  getAbsenceRequestsByEmployee(employeeId: number): Observable<AbsenceRequest[]> {
    return this.http.get<AbsenceRequest[]>(`${this.apiUrl}/absence-requests/employee/${employeeId}`);
  }

  createAbsenceRequest(request: Omit<AbsenceRequest, 'id' | 'requestedAt' | 'status'>): Observable<AbsenceRequest> {
    return this.http.post<AbsenceRequest>(`${this.apiUrl}/absence-requests`, request);
  }

  approveAbsenceRequest(id: number, reviewerId: number): Observable<AbsenceRequest> {
    return this.http.put<AbsenceRequest>(`${this.apiUrl}/absence-requests/${id}/approve`, { reviewerId });
  }

  rejectAbsenceRequest(id: number, reviewerId: number): Observable<AbsenceRequest> {
    return this.http.put<AbsenceRequest>(`${this.apiUrl}/absence-requests/${id}/reject`, { reviewerId });
  }
}
