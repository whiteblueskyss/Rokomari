import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';

// Admin Components
import AdminDashboard from './pages/admin/AdminDashboard';
import DoctorsPage from './pages/admin/DoctorsPage';
import PatientsPage from './pages/admin/PatientsPage';
import AppointmentsPage from './pages/admin/AppointmentsPage';
import SpecializationsPage from './pages/admin/SpecializationsPage';
import PrescriptionsPage from './pages/admin/PrescriptionsPage';
import AdminLogin from './pages/AdminLogin';
import AdminHome from './pages/AdminHome';

// Doctor Components
import DoctorDashboard from './pages/doctor/DoctorDashboard';
import DoctorAppointmentsPage from './pages/doctor/DoctorAppointmentsPage';
import DoctorPatientsPage from './pages/doctor/DoctorPatientsPage';
import DoctorPrescriptionsPage from './pages/doctor/DoctorPrescriptionsPage';
import DoctorLogin from './pages/DoctorLogin';

// Patient Components
import PatientDashboard from './pages/patient/PatientDashboard';
import PatientDoctorsPage from './pages/patient/PatientDoctorsPage';
import PatientPrescriptionsPage from './pages/patient/PatientPrescriptionsPage';
import PatientAppointmentsPage from './pages/patient/PatientAppointmentsPage';
import PatientLogin from './pages/PatientLogin';
import PatientRegister from './pages/PatientRegister';

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="App min-h-screen flex flex-col">
          <Routes>
            {/* Default route - Patient Login */}
            <Route path="/" element={<PatientLogin />} />

            {/* Login Routes */}
            <Route path="/patient-login" element={<PatientLogin />} />
            <Route path="/doctor-login" element={<DoctorLogin />} />
            <Route path="/admin-login" element={<AdminLogin />} />

            {/* Registration route */}
            <Route path="/register" element={<PatientRegister />} />

            {/* Home Routes (Protected) */}
            <Route path="/admin" element={
              <ProtectedRoute allowedRoles={['ADMIN']}>
                <AdminDashboard />
              </ProtectedRoute>
            } />
            <Route path="/doctor" element={
              <ProtectedRoute allowedRoles={['DOCTOR']}>
                <DoctorDashboard />
              </ProtectedRoute>
            } />
            <Route path="/patient" element={
              <ProtectedRoute allowedRoles={['PATIENT']}>
                <PatientDashboard />
              </ProtectedRoute>
            } />
            <Route path="/patient/doctors" element={
              <ProtectedRoute allowedRoles={['PATIENT']}>
                <PatientDoctorsPage />
              </ProtectedRoute>
            } />
            <Route path="/patient/prescriptions" element={
              <ProtectedRoute allowedRoles={['PATIENT']}>
                <PatientPrescriptionsPage />
              </ProtectedRoute>
            } />
            <Route path="/patient/appointments" element={
              <ProtectedRoute allowedRoles={['PATIENT']}>
                <PatientAppointmentsPage />
              </ProtectedRoute>
            } />

            {/* Doctor routes */}
            <Route path="/doctor/appointments" element={
              <ProtectedRoute allowedRoles={['DOCTOR']}>
                <DoctorAppointmentsPage />
              </ProtectedRoute>
            } />
            <Route path="/doctor/patients" element={
              <ProtectedRoute allowedRoles={['DOCTOR']}>
                <DoctorPatientsPage />
              </ProtectedRoute>
            } />
            <Route path="/doctor/prescriptions" element={
              <ProtectedRoute allowedRoles={['DOCTOR']}>
                <DoctorPrescriptionsPage />
              </ProtectedRoute>
            } />

            {/* Admin routes - flattened structure */}
            <Route path="/admin-dashboard" element={
              <ProtectedRoute allowedRoles={['ADMIN']}>
                <AdminDashboard />
              </ProtectedRoute>
            } />
            <Route path="/admin-dashboard/patients" element={
              <ProtectedRoute allowedRoles={['ADMIN']}>
                <PatientsPage />
              </ProtectedRoute>
            } />
            <Route path="/admin-dashboard/doctors" element={
              <ProtectedRoute allowedRoles={['ADMIN']}>
                <DoctorsPage />
              </ProtectedRoute>
            } />
            <Route path="/admin-dashboard/prescriptions" element={
              <ProtectedRoute allowedRoles={['ADMIN']}>
                <PrescriptionsPage />
              </ProtectedRoute>
            } />
            <Route path="/admin-dashboard/appointments" element={
              <ProtectedRoute allowedRoles={['ADMIN']}>
                <AppointmentsPage />
              </ProtectedRoute>
            } />
            <Route path="/admin-dashboard/specializations" element={
              <ProtectedRoute allowedRoles={['ADMIN']}>
                <SpecializationsPage />
              </ProtectedRoute>
            } />

            {/* Catch all route - redirect to patient login */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
