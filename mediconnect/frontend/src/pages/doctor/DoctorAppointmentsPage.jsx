import React, { useState } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { useAppointments } from '../../hooks/useApi';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { LoadingSpinner, ErrorMessage } from '../../components/common';

const DoctorAppointmentsPage = () => {
    const { user } = useAuth();
    const { data: appointments = [], loading, error } = useAppointments();
    const [selectedStatus, setSelectedStatus] = useState('all');

    // Filter appointments for this doctor
    const doctorAppointments = appointments.filter(appointment =>
        appointment.doctorId === user?.id ||
        appointment.doctor?.id === user?.id
    );

    // Filter by status
    const filteredAppointments = selectedStatus === 'all'
        ? doctorAppointments
        : doctorAppointments.filter(appointment =>
            appointment.status?.toLowerCase() === selectedStatus.toLowerCase()
        );

    const getStatusBadge = (status) => {
        const statusClasses = {
            'PENDING': 'bg-yellow-100 text-yellow-800',
            'SCHEDULED': 'bg-blue-100 text-blue-800',
            'CONFIRMED': 'bg-green-100 text-green-800',
            'COMPLETED': 'bg-gray-100 text-gray-800',
            'CANCELLED': 'bg-red-100 text-red-800'
        };
        return statusClasses[status] || 'bg-gray-100 text-gray-800';
    };

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex flex-col">
                <Header title="My Appointments" showUserInfo={true} showLogout={true} />
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
                <Header title="My Appointments" showUserInfo={true} showLogout={true} />
                <div className="flex-1 flex items-center justify-center">
                    <ErrorMessage message={error} />
                </div>
                <Footer />
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <Header title="My Appointments" showUserInfo={true} showLogout={true} />

            <main className="flex-1 container mx-auto px-4 py-8 max-w-7xl">
                <div className="mb-8">
                    <h2 className="text-3xl font-bold text-gray-800 mb-2">My Appointments</h2>
                    <p className="text-gray-600">
                        Manage your patient appointments and schedules
                    </p>
                </div>

                {/* Filter Controls */}
                <div className="mb-6 flex flex-wrap gap-4 items-center">
                    <div className="flex items-center space-x-2">
                        <label htmlFor="status-filter" className="text-sm font-medium text-gray-700">
                            Filter by status:
                        </label>
                        <select
                            id="status-filter"
                            value={selectedStatus}
                            onChange={(e) => setSelectedStatus(e.target.value)}
                            className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
                        >
                            <option value="all">All Appointments</option>
                            <option value="pending">Pending</option>
                            <option value="scheduled">Scheduled</option>
                            <option value="confirmed">Confirmed</option>
                            <option value="completed">Completed</option>
                            <option value="cancelled">Cancelled</option>
                        </select>
                    </div>
                    <div className="text-sm text-gray-500">
                        Showing {filteredAppointments.length} of {doctorAppointments.length} appointments
                    </div>
                </div>

                {/* Appointments Grid */}
                {filteredAppointments.length === 0 ? (
                    <div className="bg-white rounded-lg shadow-md p-12 text-center">
                        <svg className="mx-auto h-12 w-12 text-gray-400 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                        </svg>
                        <h3 className="text-lg font-medium text-gray-900 mb-2">No appointments found</h3>
                        <p className="text-gray-500">
                            {selectedStatus === 'all'
                                ? 'You don\'t have any appointments yet.'
                                : `No appointments with status "${selectedStatus}".`
                            }
                        </p>
                    </div>
                ) : (
                    <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
                        {filteredAppointments.map((appointment) => (
                            <div key={appointment.id} className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
                                <div className="flex justify-between items-start mb-4">
                                    <div className="flex items-center">
                                        <div className="p-2 bg-purple-100 rounded-full mr-3">
                                            <svg className="w-5 h-5 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                                            </svg>
                                        </div>
                                        <div>
                                            <h3 className="font-semibold text-gray-900">
                                                {appointment.patient?.name || appointment.patientName || 'Unknown Patient'}
                                            </h3>
                                            <p className="text-sm text-gray-600">
                                                Patient ID: {appointment.patient?.id || appointment.patientId || 'N/A'}
                                            </p>
                                        </div>
                                    </div>
                                    <span className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusBadge(appointment.status)}`}>
                                        {appointment.status}
                                    </span>
                                </div>

                                <div className="space-y-3">
                                    <div className="flex items-center text-sm text-gray-600">
                                        <svg className="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                                        </svg>
                                        <span className="font-medium">Date:</span>
                                        <span className="ml-1">
                                            {new Date(appointment.appointmentDate).toLocaleDateString()}
                                        </span>
                                    </div>

                                    <div className="flex items-center text-sm text-gray-600">
                                        <svg className="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                                        </svg>
                                        <span className="font-medium">Time:</span>
                                        <span className="ml-1">{appointment.appointmentTime || 'Not specified'}</span>
                                    </div>

                                    {appointment.reason && (
                                        <div className="text-sm text-gray-600">
                                            <span className="font-medium">Reason:</span>
                                            <p className="mt-1 text-gray-700">{appointment.reason}</p>
                                        </div>
                                    )}

                                    {appointment.notes && (
                                        <div className="text-sm text-gray-600">
                                            <span className="font-medium">Notes:</span>
                                            <p className="mt-1 text-gray-700">{appointment.notes}</p>
                                        </div>
                                    )}
                                </div>

                                <div className="mt-4 pt-4 border-t border-gray-200">
                                    <div className="flex justify-between items-center text-xs text-gray-500">
                                        <span>Appointment ID: {appointment.id}</span>
                                        {appointment.createdDate && (
                                            <span>Created: {new Date(appointment.createdDate).toLocaleDateString()}</span>
                                        )}
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                )}

                {/* Summary Stats */}
                {doctorAppointments.length > 0 && (
                    <div className="mt-8 bg-white rounded-lg shadow-md p-6">
                        <h3 className="text-lg font-semibold text-gray-800 mb-4">Appointment Summary</h3>
                        <div className="grid grid-cols-2 md:grid-cols-5 gap-4">
                            <div className="text-center">
                                <p className="text-2xl font-bold text-blue-600">{doctorAppointments.length}</p>
                                <p className="text-sm text-gray-600">Total</p>
                            </div>
                            <div className="text-center">
                                <p className="text-2xl font-bold text-yellow-600">
                                    {doctorAppointments.filter(apt => apt.status === 'PENDING').length}
                                </p>
                                <p className="text-sm text-gray-600">Pending</p>
                            </div>
                            <div className="text-center">
                                <p className="text-2xl font-bold text-green-600">
                                    {doctorAppointments.filter(apt => apt.status === 'SCHEDULED' || apt.status === 'CONFIRMED').length}
                                </p>
                                <p className="text-sm text-gray-600">Scheduled</p>
                            </div>
                            <div className="text-center">
                                <p className="text-2xl font-bold text-gray-600">
                                    {doctorAppointments.filter(apt => apt.status === 'COMPLETED').length}
                                </p>
                                <p className="text-sm text-gray-600">Completed</p>
                            </div>
                            <div className="text-center">
                                <p className="text-2xl font-bold text-red-600">
                                    {doctorAppointments.filter(apt => apt.status === 'CANCELLED').length}
                                </p>
                                <p className="text-sm text-gray-600">Cancelled</p>
                            </div>
                        </div>
                    </div>
                )}
            </main>

            <Footer />
        </div>
    );
};

export default DoctorAppointmentsPage;