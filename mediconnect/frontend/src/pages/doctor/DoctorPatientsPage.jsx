import React, { useState } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { usePatients, useAppointments } from '../../hooks/useApi';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { LoadingSpinner, ErrorMessage } from '../../components/common';

const DoctorPatientsPage = () => {
    const { user } = useAuth();
    const { data: patients = [], loading: patientsLoading, error: patientsError } = usePatients();
    const { data: appointments = [], loading: appointmentsLoading, error: appointmentsError } = useAppointments();
    const [searchTerm, setSearchTerm] = useState('');

    // Get appointments for this doctor to find their patients
    const doctorAppointments = appointments.filter(appointment =>
        appointment.doctorId === user?.id ||
        appointment.doctor?.id === user?.id
    );

    // Get unique patient IDs from doctor's appointments
    const doctorPatientIds = [...new Set(doctorAppointments.map(apt =>
        apt.patientId || apt.patient?.id
    ).filter(Boolean))];

    // Filter patients to show only those who have appointments with this doctor
    const doctorPatients = patients.filter(patient =>
        doctorPatientIds.includes(patient.id)
    );

    // Filter patients based on search term
    const filteredPatients = doctorPatients.filter(patient =>
        patient.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        patient.firstName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        patient.lastName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        patient.email?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        patient.phone?.includes(searchTerm)
    );

    // Get patient statistics
    const getPatientStats = (patientId) => {
        const patientAppointments = doctorAppointments.filter(apt =>
            apt.patientId === patientId || apt.patient?.id === patientId
        );

        return {
            totalAppointments: patientAppointments.length,
            completedAppointments: patientAppointments.filter(apt => apt.status === 'COMPLETED').length,
            lastAppointment: patientAppointments
                .sort((a, b) => new Date(b.appointmentDate) - new Date(a.appointmentDate))[0]
        };
    };

    const loading = patientsLoading || appointmentsLoading;
    const error = patientsError || appointmentsError;

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex flex-col">
                <Header title="My Patients" showUserInfo={true} showLogout={true} />
                <div className="flex-1 flex items-center justify-center">
                    <LoadingSpinner />
                </div>
                <Footer />
            </div>
        );
    }

    if (error) {
        return (
            <div className="min-h-screen bg-gray-50 flex flex-col">
                <Header title="My Patients" showUserInfo={true} showLogout={true} />
                <div className="flex-1 flex items-center justify-center">
                    <ErrorMessage message={error} />
                </div>
                <Footer />
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <Header title="My Patients" showUserInfo={true} showLogout={true} />

            <main className="flex-1 container mx-auto px-4 py-8 max-w-7xl">
                <div className="mb-8">
                    <h2 className="text-3xl font-bold text-gray-800 mb-2">My Patients</h2>
                    <p className="text-gray-600">
                        View and manage your patient information and appointment history
                    </p>
                </div>

                {/* Search and Stats */}
                <div className="mb-6 flex flex-col sm:flex-row sm:justify-between sm:items-center gap-4">
                    <div className="relative">
                        <svg className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                        </svg>
                        <input
                            type="text"
                            placeholder="Search patients by name, email, or phone..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            className="pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500 w-full sm:w-80"
                        />
                    </div>
                    <div className="text-sm text-gray-500">
                        Showing {filteredPatients.length} of {doctorPatients.length} patients
                    </div>
                </div>

                {/* Patients Grid */}
                {filteredPatients.length === 0 ? (
                    <div className="bg-white rounded-lg shadow-md p-12 text-center">
                        <svg className="mx-auto h-12 w-12 text-gray-400 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                        </svg>
                        <h3 className="text-lg font-medium text-gray-900 mb-2">No patients found</h3>
                        <p className="text-gray-500">
                            {searchTerm
                                ? `No patients match your search term "${searchTerm}".`
                                : 'You don\'t have any patients yet.'
                            }
                        </p>
                    </div>
                ) : (
                    <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
                        {filteredPatients.map((patient) => {
                            const stats = getPatientStats(patient.id);
                            return (
                                <div key={patient.id} className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
                                    <div className="flex items-start justify-between mb-4">
                                        <div className="flex items-center">
                                            <div className="p-3 bg-purple-100 rounded-full mr-3">
                                                <svg className="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                                                </svg>
                                            </div>
                                            <div>
                                                <h3 className="font-semibold text-gray-900">
                                                    {patient.name || `${patient.firstName || ''} ${patient.lastName || ''}`.trim() || 'Unknown Patient'}
                                                </h3>
                                                <p className="text-sm text-gray-600">
                                                    ID: {patient.id}
                                                </p>
                                            </div>
                                        </div>
                                    </div>

                                    <div className="space-y-3">
                                        {patient.email && (
                                            <div className="flex items-center text-sm text-gray-600">
                                                <svg className="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 8l7.89 4.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                                                </svg>
                                                <span>{patient.email}</span>
                                            </div>
                                        )}

                                        {patient.phone && (
                                            <div className="flex items-center text-sm text-gray-600">
                                                <svg className="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
                                                </svg>
                                                <span>{patient.phone}</span>
                                            </div>
                                        )}

                                        {patient.age && (
                                            <div className="flex items-center text-sm text-gray-600">
                                                <svg className="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                                                </svg>
                                                <span>Age: {patient.age}</span>
                                            </div>
                                        )}

                                        {patient.gender && (
                                            <div className="flex items-center text-sm text-gray-600">
                                                <svg className="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                                                </svg>
                                                <span>Gender: {patient.gender}</span>
                                            </div>
                                        )}
                                    </div>

                                    {/* Patient Statistics */}
                                    <div className="mt-4 pt-4 border-t border-gray-200">
                                        <div className="grid grid-cols-2 gap-4 text-center">
                                            <div>
                                                <p className="text-lg font-bold text-purple-600">{stats.totalAppointments}</p>
                                                <p className="text-xs text-gray-600">Total Appointments</p>
                                            </div>
                                            <div>
                                                <p className="text-lg font-bold text-green-600">{stats.completedAppointments}</p>
                                                <p className="text-xs text-gray-600">Completed</p>
                                            </div>
                                        </div>
                                        {stats.lastAppointment && (
                                            <div className="mt-3 text-center">
                                                <p className="text-xs text-gray-500">
                                                    Last visit: {new Date(stats.lastAppointment.appointmentDate).toLocaleDateString()}
                                                </p>
                                            </div>
                                        )}
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                )}

                {/* Summary Statistics */}
                {doctorPatients.length > 0 && (
                    <div className="mt-8 bg-white rounded-lg shadow-md p-6">
                        <h3 className="text-lg font-semibold text-gray-800 mb-4">Patient Overview</h3>
                        <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
                            <div className="text-center">
                                <div className="p-3 bg-purple-100 rounded-full inline-flex mb-2">
                                    <svg className="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                                    </svg>
                                </div>
                                <p className="text-2xl font-bold text-gray-900">{doctorPatients.length}</p>
                                <p className="text-sm text-gray-600">Total Patients</p>
                            </div>

                            <div className="text-center">
                                <div className="p-3 bg-blue-100 rounded-full inline-flex mb-2">
                                    <svg className="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                                    </svg>
                                </div>
                                <p className="text-2xl font-bold text-gray-900">{doctorAppointments.length}</p>
                                <p className="text-sm text-gray-600">Total Appointments</p>
                            </div>

                            <div className="text-center">
                                <div className="p-3 bg-green-100 rounded-full inline-flex mb-2">
                                    <svg className="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                                    </svg>
                                </div>
                                <p className="text-2xl font-bold text-gray-900">
                                    {doctorAppointments.filter(apt => apt.status === 'COMPLETED').length}
                                </p>
                                <p className="text-sm text-gray-600">Completed Visits</p>
                            </div>

                            <div className="text-center">
                                <div className="p-3 bg-yellow-100 rounded-full inline-flex mb-2">
                                    <svg className="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                                    </svg>
                                </div>
                                <p className="text-2xl font-bold text-gray-900">
                                    {doctorAppointments.filter(apt => apt.status === 'PENDING' || apt.status === 'SCHEDULED').length}
                                </p>
                                <p className="text-sm text-gray-600">Upcoming</p>
                            </div>
                        </div>
                    </div>
                )}
            </main>

            <Footer />
        </div>
    );
};

export default DoctorPatientsPage;