import React from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { useAppointments, usePrescriptions } from '../../hooks/useApi';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { LoadingSpinner, ErrorMessage } from '../../components/common';

const DoctorDashboard = () => {
    const { user } = useAuth();

    // Get appointments for this doctor
    const {
        data: appointments = [],
        loading: appointmentsLoading,
        error: appointmentsError
    } = useAppointments();

    // Get prescriptions created by this doctor
    const {
        data: prescriptions = [],
        loading: prescriptionsLoading,
        error: prescriptionsError
    } = usePrescriptions();

    // Filter appointments and prescriptions for current doctor
    const doctorAppointments = appointments.filter(appointment =>
        appointment.doctorId === user?.id ||
        appointment.doctor?.id === user?.id
    );

    const doctorPrescriptions = prescriptions.filter(prescription =>
        prescription.doctorId === user?.id ||
        prescription.doctor?.id === user?.id
    );

    // Recent appointments (last 5)
    const recentAppointments = doctorAppointments
        .sort((a, b) => new Date(b.appointmentDate) - new Date(a.appointmentDate))
        .slice(0, 5);

    // Today's appointments
    const today = new Date().toISOString().split('T')[0];
    const todayAppointments = doctorAppointments.filter(appointment =>
        appointment.appointmentDate?.split('T')[0] === today ||
        appointment.appointmentDate?.split(' ')[0] === today
    );

    const getStatusStyle = (status) => {
        const styles = {
            'SCHEDULED': { bg: 'bg-green-50', badge: 'bg-green-200 text-green-800', circle: 'bg-green-100', text: 'text-green-600' },
            'IN_PROGRESS': { bg: 'bg-yellow-50', badge: 'bg-yellow-200 text-yellow-800', circle: 'bg-yellow-100', text: 'text-yellow-600' },
            'COMPLETED': { bg: 'bg-gray-50', badge: 'bg-gray-200 text-gray-800', circle: 'bg-gray-100', text: 'text-gray-600' },
            'CANCELLED': { bg: 'bg-red-50', badge: 'bg-red-200 text-red-800', circle: 'bg-red-100', text: 'text-red-600' }
        };
        return styles[status] || styles['SCHEDULED'];
    };

    if (appointmentsLoading || prescriptionsLoading) {
        return (
            <div className="min-h-screen bg-gray-50 flex flex-col">
                <Header
                    title="Doctor Dashboard"
                    showUserInfo={true}
                    showLogout={true}
                />
                <div className="flex-1 flex items-center justify-center">
                    <LoadingSpinner />
                </div>
                <Footer />
            </div>
        );
    }

    if (appointmentsError || prescriptionsError) {
        return (
            <div className="min-h-screen bg-gray-50 flex flex-col">
                <Header
                    title="Doctor Dashboard"
                    showUserInfo={true}
                    showLogout={true}
                />
                <div className="flex-1 flex items-center justify-center">
                    <ErrorMessage message={appointmentsError || prescriptionsError} />
                </div>
                <Footer />
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <Header
                title="Doctor Dashboard"
                showUserInfo={true}
                showLogout={true}
            />

            <main className="flex-1 container mx-auto px-4 py-8 max-w-7xl">
                {/* Welcome Section */}
                <div className="mb-8">
                    <h2 className="text-3xl font-bold text-gray-800 mb-2">
                        Welcome back, Dr. {user?.name || user?.firstName || 'Doctor'}!
                    </h2>
                    <p className="text-gray-600">
                        Here's your medical practice overview for today.
                    </p>
                </div>

                {/* Stats Cards */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
                    <div className="bg-white rounded-lg shadow-md p-6">
                        <div className="flex items-center">
                            <div className="p-3 rounded-full bg-blue-100 text-blue-600">
                                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                                </svg>
                            </div>
                            <div className="ml-4">
                                <p className="text-sm font-medium text-gray-600">Today's Appointments</p>
                                <p className="text-2xl font-bold text-gray-900">{todayAppointments.length}</p>
                            </div>
                        </div>
                    </div>

                    <div className="bg-white rounded-lg shadow-md p-6">
                        <div className="flex items-center">
                            <div className="p-3 rounded-full bg-green-100 text-green-600">
                                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                                </svg>
                            </div>
                            <div className="ml-4">
                                <p className="text-sm font-medium text-gray-600">Total Prescriptions</p>
                                <p className="text-2xl font-bold text-gray-900">{doctorPrescriptions.length}</p>
                            </div>
                        </div>
                    </div>

                    <div className="bg-white rounded-lg shadow-md p-6">
                        <div className="flex items-center">
                            <div className="p-3 rounded-full bg-purple-100 text-purple-600">
                                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                                </svg>
                            </div>
                            <div className="ml-4">
                                <p className="text-sm font-medium text-gray-600">Total Appointments</p>
                                <p className="text-2xl font-bold text-gray-900">{doctorAppointments.length}</p>
                            </div>
                        </div>
                    </div>

                    <div className="bg-white rounded-lg shadow-md p-6">
                        <div className="flex items-center">
                            <div className="p-3 rounded-full bg-yellow-100 text-yellow-600">
                                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                                </svg>
                            </div>
                            <div className="ml-4">
                                <p className="text-sm font-medium text-gray-600">Pending Appointments</p>
                                <p className="text-2xl font-bold text-gray-900">
                                    {doctorAppointments.filter(apt => apt.status === 'PENDING' || apt.status === 'SCHEDULED').length}
                                </p>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Today's Schedule */}
                {todayAppointments.length > 0 && (
                    <div className="mb-8 bg-white rounded-lg shadow-md">
                        <div className="px-6 py-4 border-b border-gray-200">
                            <h3 className="text-lg font-semibold text-gray-800">Today's Schedule</h3>
                        </div>
                        <div className="p-6">
                            <div className="grid gap-4">
                                {todayAppointments
                                    .sort((a, b) => (a.appointmentTime || '').localeCompare(b.appointmentTime || ''))
                                    .map((appointment) => (
                                        <div key={appointment.id} className="flex items-center justify-between p-4 border border-gray-200 rounded-lg">
                                            <div className="flex items-center">
                                                <div className="p-2 bg-blue-100 rounded-full mr-4">
                                                    <svg className="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                                                    </svg>
                                                </div>
                                                <div>
                                                    <p className="font-medium text-gray-800">
                                                        {appointment.patient?.name || appointment.patientName || 'Unknown Patient'}
                                                    </p>
                                                    <p className="text-sm text-gray-600">
                                                        {appointment.appointmentTime} - {appointment.reason || 'General consultation'}
                                                    </p>
                                                </div>
                                            </div>
                                            <span className={`px-3 py-1 rounded-full text-sm font-medium ${appointment.status === 'COMPLETED' ? 'bg-green-100 text-green-800' :
                                                    appointment.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' :
                                                        'bg-blue-100 text-blue-800'
                                                }`}>
                                                {appointment.status}
                                            </span>
                                        </div>
                                    ))}
                            </div>
                        </div>
                    </div>
                )}

                {/* Recent Activity */}
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                    {/* Recent Appointments */}
                    <div className="bg-white rounded-lg shadow-md">
                        <div className="px-6 py-4 border-b border-gray-200">
                            <h3 className="text-lg font-semibold text-gray-800">Recent Appointments</h3>
                        </div>
                        <div className="p-6">
                            {recentAppointments.length === 0 ? (
                                <p className="text-gray-500 text-center py-4">No recent appointments</p>
                            ) : (
                                <div className="space-y-4">
                                    {recentAppointments.map((appointment) => (
                                        <div key={appointment.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                                            <div>
                                                <p className="font-medium text-gray-800">
                                                    {appointment.patient?.name || appointment.patientName || 'Unknown Patient'}
                                                </p>
                                                <p className="text-sm text-gray-600">
                                                    {new Date(appointment.appointmentDate).toLocaleDateString()} at {appointment.appointmentTime}
                                                </p>
                                            </div>
                                            <span className={`px-2 py-1 rounded-full text-xs font-medium ${appointment.status === 'COMPLETED' ? 'bg-green-100 text-green-800' :
                                                    appointment.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' :
                                                        'bg-blue-100 text-blue-800'
                                                }`}>
                                                {appointment.status}
                                            </span>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>
                    </div>

                    {/* Recent Prescriptions */}
                    <div className="bg-white rounded-lg shadow-md">
                        <div className="px-6 py-4 border-b border-gray-200">
                            <h3 className="text-lg font-semibold text-gray-800">Recent Prescriptions</h3>
                        </div>
                        <div className="p-6">
                            {doctorPrescriptions.length === 0 ? (
                                <p className="text-gray-500 text-center py-4">No prescriptions created yet</p>
                            ) : (
                                <div className="space-y-4">
                                    {doctorPrescriptions.slice(0, 5).map((prescription) => (
                                        <div key={prescription.id} className="p-3 bg-gray-50 rounded-lg">
                                            <div className="flex justify-between items-start mb-2">
                                                <p className="font-medium text-gray-800">
                                                    {prescription.patient?.name || prescription.patientName || 'Unknown Patient'}
                                                </p>
                                                <span className="text-xs text-gray-500">
                                                    {new Date(prescription.createdDate || prescription.dateCreated).toLocaleDateString()}
                                                </span>
                                            </div>
                                            <p className="text-sm text-gray-600 mb-1">
                                                <strong>Medication:</strong> {prescription.medicationName || prescription.medication}
                                            </p>
                                            <p className="text-sm text-gray-600">
                                                <strong>Dosage:</strong> {prescription.dosage}
                                            </p>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </main>

            <Footer />
        </div>
    );
};

export default DoctorDashboard;