// src/app/core/services/api.service.ts
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class Api {
  private readonly baseUrl = 'http://localhost:8080/api';

  readonly auth = {
    login: `${this.baseUrl}/auth/login`,
    register: `${this.baseUrl}/auth/register`,
    profile: `${this.baseUrl}/auth/profile`
  };

  readonly users = {
    all: `${this.baseUrl}/users`,
    byId: (id: number) => `${this.baseUrl}/users/${id}`,
    update: (id: number) => `${this.baseUrl}/users/${id}`,
    delete: (id: number) => `${this.baseUrl}/users/${id}`,
    updateRole: (id: number) => `${this.baseUrl}/users/${id}/role`,
    resetRole: (id: number) => `${this.baseUrl}/users/${id}/role`
  };

  readonly buildings = {
    all: `${this.baseUrl}/buildings`,
    byId: (id: number) => `${this.baseUrl}/buildings/${id}`,
    byUserId: (userId: number) => `${this.baseUrl}/buildings/user/${userId}`,
    byUser: `${this.baseUrl}/buildings/user`,
    add: `${this.baseUrl}/buildings`,
    update: (id: number) => `${this.baseUrl}/buildings/${id}`,
    delete: (id: number) => `${this.baseUrl}/buildings/${id}`,
    updateCondition: `${this.baseUrl}/buildings/condition`
  };

  readonly addresses = {
    all: `${this.baseUrl}/addresses`,
    byBuilding: (buildingId: number) => `${this.baseUrl}/addresses/building/${buildingId}`,
    add: `${this.baseUrl}/addresses`,
    update: (id: number) => `${this.baseUrl}/addresses/${id}`,
    delete: (id: number) => `${this.baseUrl}/addresses/${id}`,
    bindToBuilding: (id: number) => `${this.baseUrl}/addresses/${id}/building`
  };

  readonly maintenances = {
    all: `${this.baseUrl}/maintenances`,
    byId: (id: number) => `${this.baseUrl}/maintenances/${id}`,
    byBuilding: (buildingId: number) => `${this.baseUrl}/maintenances/building/${buildingId}`,
    add: `${this.baseUrl}/maintenances`,
    update: (id: number) => `${this.baseUrl}/maintenances/${id}`,
    delete: (id: number) => `${this.baseUrl}/maintenances/${id}`,
    calculateCost: `${this.baseUrl}/maintenances/calculate`
  };

  readonly payments = {
    all: `${this.baseUrl}/payments`,
    byId: (id: number) => `${this.baseUrl}/payments/${id}`,
    byMaintenanceId: (maintenanceId: number) => `${this.baseUrl}/payments/maintenance/${maintenanceId}`,
    add: `${this.baseUrl}/payments`,
    update: (id: number) => `${this.baseUrl}/payments/${id}`,
    delete: (id: number) => `${this.baseUrl}/payments/${id}`,
    paypal: {
      create: `${this.baseUrl}/payments/paypal`,
      success: `${this.baseUrl}/payments/success`,
      cancel: `${this.baseUrl}/payments/cancel`
    }
  };

  readonly sensors = {
    all: `${this.baseUrl}/sensors`,
    byId: (id: number) => `${this.baseUrl}/sensors/${id}`,
    byBuilding: (buildingId: number) => `${this.baseUrl}/sensors/building/${buildingId}`,
    add: `${this.baseUrl}/sensors`,
    update: (id: number) => `${this.baseUrl}/sensors/${id}`,
    delete: (id: number) => `${this.baseUrl}/sensors/${id}`,
    updateStatus: (id: number) => `${this.baseUrl}/sensors/${id}/status`,
    updateBuilding: (id: number) => `${this.baseUrl}/sensors/${id}/building`,
    updateGeneralStatus: `${this.baseUrl}/sensors/status`,
    notify: `${this.baseUrl}/sensors/notify`,
    enabled: `${this.baseUrl}/sensors/enabled`
  };

  readonly measurements = {
    all: `${this.baseUrl}/measurements`,
    add: `${this.baseUrl}/measurements`,
    byId: (id: number) => `${this.baseUrl}/measurements/${id}`,
    delete: (id: number) => `${this.baseUrl}/measurements/${id}`,
    bySensor: (sensorId: number) => `${this.baseUrl}/measurements/sensor/${sensorId}`,
    bySensorAndUnit: (sensorId: number) => `${this.baseUrl}/measurements/sensor/${sensorId}/unit`
  };

  readonly alarms = {
    all: `${this.baseUrl}/alarms`,
    byId: (id: number) => `${this.baseUrl}/alarms/${id}`,
    add: `${this.baseUrl}/alarms`,
    update: (id: number) => `${this.baseUrl}/alarms/${id}`,
    delete: (id: number) => `${this.baseUrl}/alarms/${id}`,
    notify: `${this.baseUrl}/alarms/notify`,
    byType: (type: string) => `${this.baseUrl}/alarms/type/${type}`,
    bySensor: (sensorId: number) => `${this.baseUrl}/alarms/sensor/${sensorId}`,
    byBuilding: (buildingId: number) => `${this.baseUrl}/alarms/building/${buildingId}`
  };

  readonly admin = {
    sensorSettings: {
      updateServiceCost: (id: number) => `${this.baseUrl}/admin/sensor-settings/${id}/service-cost`,
      updateMeasurementFrequency: (id: number) => `${this.baseUrl}/admin/sensor-settings/${id}/measurement-frequency`,
      updateCriticalValue: (id: number) => `${this.baseUrl}/admin/sensor-settings/${id}/critical-value`
    },
    systemSettings: {
      all: `${this.baseUrl}/admin/system-settings`,
      backup: `${this.baseUrl}/admin/system-settings/backup`,
      restore: `${this.baseUrl}/admin/system-settings/restore`,
      update: (key: string) => `${this.baseUrl}/admin/system-settings/${key}`,
      create: `${this.baseUrl}/admin/system-settings`
    }
  };
}
