import React from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { useAppointments, useDoctors, usePrescriptions, usePrescriptionsByPatient } from '../../hooks/useApi';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { LoadingSpinner, ErrorMessage } from '../../components/common';

const PatientDashboard = () => {
    const { user } = useAuth();


    // Get data using our existing hooks
    const {
        data: appointments = [],
        loading: appointmentsLoading,
        error: appointmentsError
    } = useAppointments();

    const {
        data: doctors = [],
        loading: doctorsLoading,
        error: doctorsError
    } = useDoctors();

    const {
        data: prescriptions = [],
        loading: prescriptionsLoading,
        error: prescriptionsError
    } = usePrescriptions();

    console.log("Here is console log for appointments, doctors, prescriptions:", appointments, doctors, prescriptions);
    console.log("Current user:", user);

    // Filter data for current patient
    const patientAppointments = appointments.filter(appointment =>
        appointment.patientId === user?.id
    );

    const patientPrescriptions = prescriptions.filter(prescription =>
        prescription.patientId === user?.id
    );


    console.log("AFter filter Here is console log for appointments, doctors, prescriptions:", appointments, doctors, prescriptions);
    console.log("Current user:", user);


    // Calculate stats
    const upcomingAppointments = patientAppointments.filter(apt =>
        new Date(apt.appointmentDate) > new Date() &&
        (apt.status === 'SCHEDULED' || apt.status === 'PENDING')
    );

    const completedAppointments = patientAppointments.filter(apt =>
        apt.status === 'COMPLETED'
    );

    const activePrescriptions = patientPrescriptions.filter(prescription =>
        prescription.status === 'ACTIVE'
    );

    console.log("Filtered data:", {
        patientAppointments: patientAppointments.length,
        patientPrescriptions: patientPrescriptions.length,
        upcomingAppointments: upcomingAppointments.length,
        completedAppointments: completedAppointments.length,
        activePrescriptions: activePrescriptions.length
    });

    const loading = appointmentsLoading || doctorsLoading || prescriptionsLoading;
    const error = appointmentsError || doctorsError || prescriptionsError;

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex flex-col">
                <Header title="Patient Dashboard" showUserInfo={true} showLogout={true} />
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
                <Header title="Patient Dashboard" showUserInfo={true} showLogout={true} />
                <div className="flex-1 flex items-center justify-center">
                    <ErrorMessage message={error} />
                </div>
                <Footer />
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <Header title="Patient Dashboard" showUserInfo={true} showLogout={true} />

            <main className="flex-1 container mx-auto px-4 py-8 max-w-7xl">
                {/* Welcome Section */}
                <div className="mb-8">
                    <h2 className="text-3xl font-bold text-gray-800 mb-2">
                        Welcome back, {user?.name || 'Patient'}!
                    </h2>
                    <p className="text-gray-600">
                        Manage your appointments and view your medical history.
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
                                <p className="text-sm font-medium text-gray-600">Upcoming Appointments</p>
                                <p className="text-2xl font-bold text-gray-900">{upcomingAppointments.length}</p>
                            </div>
                        </div>
                    </div>

                    <div className="bg-white rounded-lg shadow-md p-6">
                        <div className="flex items-center">
                            <div className="p-3 rounded-full bg-green-100 text-green-600">
                                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                                </svg>
                            </div>
                            <div className="ml-4">
                                <p className="text-sm font-medium text-gray-600">Completed Visits</p>
                                <p className="text-2xl font-bold text-gray-900">{completedAppointments.length}</p>
                            </div>
                        </div>
                    </div>

                    <div className="bg-white rounded-lg shadow-md p-6">
                        <div className="flex items-center">
                            <div className="p-3 rounded-full bg-purple-100 text-purple-600">
                                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                                </svg>
                            </div>
                            <div className="ml-4">
                                <p className="text-sm font-medium text-gray-600">Active Prescriptions</p>
                                <p className="text-2xl font-bold text-gray-900">{activePrescriptions.length}</p>
                            </div>
                        </div>
                    </div>

                    <div className="bg-white rounded-lg shadow-md p-6">
                        <div className="flex items-center">
                            <div className="p-3 rounded-full bg-yellow-100 text-yellow-600">
                                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                                </svg>
                            </div>
                            <div className="ml-4">
                                <p className="text-sm font-medium text-gray-600">Available Doctors</p>
                                <p className="text-2xl font-bold text-gray-900">{doctors.length}</p>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Recent Appointments */}
                {patientAppointments.length > 0 && (
                    <div className="bg-white rounded-lg shadow-md mb-8">
                        <div className="px-6 py-4 border-b border-gray-200">
                            <h3 className="text-lg font-semibold text-gray-800">Recent Appointments</h3>
                        </div>
                        <div className="p-6">
                            <div className="space-y-4">
                                {patientAppointments.slice(0, 3).map((appointment) => (
                                    <div key={appointment.id} className="flex items-center justify-between p-4 border border-gray-200 rounded-lg">
                                        <div className="flex items-center">
                                            <div className="p-2 bg-blue-100 rounded-full mr-4">
                                                <svg className="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                                                </svg>
                                            </div>
                                            <div>
                                                <p className="font-medium text-gray-800">
                                                    {appointment.doctorName}
                                                </p>
                                                <p className="text-sm text-gray-600">
                                                    {appointment.appointmentDate} at {appointment.appointmentTime}
                                                </p>
                                                <p className="text-sm text-gray-500">
                                                    {appointment.reason}
                                                </p>
                                            </div>
                                        </div>
                                        <span className={`px-3 py-1 rounded-full text-sm font-medium ${appointment.status === 'COMPLETED'
                                            ? 'bg-green-100 text-green-800'
                                            : appointment.status === 'SCHEDULED'
                                                ? 'bg-blue-100 text-blue-800'
                                                : 'bg-yellow-100 text-yellow-800'
                                            }`}>
                                            {appointment.status}
                                        </span>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                )}

                {/* Recent Prescriptions */}
                {patientPrescriptions.length > 0 && (
                    <div className="bg-white rounded-lg shadow-md mb-8">
                        <div className="px-6 py-4 border-b border-gray-200">
                            <h3 className="text-lg font-semibold text-gray-800">Recent Prescriptions</h3>
                        </div>
                        <div className="p-6">
                            <div className="space-y-4">
                                {patientPrescriptions.slice(0, 3).map((prescription) => (
                                    <div key={prescription.id} className="flex items-center justify-between p-4 border border-gray-200 rounded-lg">
                                        <div className="flex items-center">
                                            <div className="p-2 bg-purple-100 rounded-full mr-4">
                                                <svg className="w-4 h-4 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                                                </svg>
                                            </div>
                                            <div>
                                                <p className="font-medium text-gray-800">
                                                    Prescription #{prescription.id}
                                                </p>
                                                <p className="text-sm text-gray-600">
                                                    By {prescription.doctorName}
                                                </p>
                                                <p className="text-sm text-gray-500">
                                                    {prescription.prescriptionDate}
                                                </p>
                                            </div>
                                        </div>
                                        <span className={`px-3 py-1 rounded-full text-sm font-medium ${prescription.status === 'ACTIVE'
                                            ? 'bg-green-100 text-green-800'
                                            : 'bg-gray-100 text-gray-800'
                                            }`}>
                                            {prescription.status}
                                        </span>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                )}

                {/* No Data Message */}
                {patientAppointments.length === 0 && patientPrescriptions.length === 0 && (
                    <div className="bg-white rounded-lg shadow-md p-8 text-center">
                        <div className="text-gray-400 mb-4">
                            <svg className="w-16 h-16 mx-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                            </svg>
                        </div>
                        <h3 className="text-lg font-medium text-gray-900 mb-2">No Medical History Yet</h3>
                        <p className="text-gray-500">
                            You haven't had any appointments or prescriptions yet. Book your first appointment to get started!
                        </p>
                    </div>
                )}
            </main>

            <Footer />
        </div>
    );

};

export default PatientDashboard;