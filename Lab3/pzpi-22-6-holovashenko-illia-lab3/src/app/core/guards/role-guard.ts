import { inject } from '@angular/core';
import { CanActivateFn, ActivatedRouteSnapshot, Router } from '@angular/router';

export const RoleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const router = inject(Router);
  const expectedRoles = route.data['roles'] as string[];
  const role = localStorage.getItem('userRole');

  return expectedRoles?.includes(role || '') ? true : router.parseUrl('/unauthorized');
};
