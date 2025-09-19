import { Component } from '@angular/core';
import { RoleSwitcherComponent } from '../role-switcher/role-switcher.component';
import { RoleService } from '../../services/role.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RoleSwitcherComponent],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  constructor(protected roleService: RoleService) {}
}
