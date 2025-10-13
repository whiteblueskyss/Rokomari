import React, { useState } from 'react';
import { useAppointments } from '../../hooks/useApi';
import { useAuth } from '../../contexts/AuthContext';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { LoadingSpinner, ErrorMessage } from '../../components/common';

const PatientAppointmentsPage = () => {
    const { user } = useAuth();
    const { data: appointments, loading, error, refetch } = useAppointments();
    const [searchTerm, setSearchTerm] = useState('');
    const [statusFilter, setStatusFilter] = useState('');

    // Filter appointments for current patient
    const patientAppointments = appointments.filter(appointment =>
        appointment.patientId === user?.id || appointment.patient?.id === user?.id
    );

    // Further filter based on search term and status
    const filteredAppointments = patientAppointments.filter(appointment => {
        const matchesSearch = appointment.doctorName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
            appointment.problemDescription?.toLowerCase().includes(searchTerm.toLowerCase()) ||
            appointment.status?.toLowerCase().includes(searchTerm.toLowerCase());
        const matchesStatus = statusFilter === '' || appointment.status?.toLowerCase() === statusFilter.toLowerCase();
        return matchesSearch && matchesStatus;
    });

    // Get unique statuses for filter
    const statuses = [...new Set(patientAppointments.map(apt => apt.status).filter(Boolean))];

    // Categorize appointments
    const upcomingAppointments = filteredAppointments.filter(apt => {
        const appointmentDate = new Date(apt.visitingDate || apt.appointmentDate);
        return appointmentDate > new Date() && apt.status?.toLowerCase() !== 'cancelled';
    });

    const pastAppointments = filteredAppointments.filter(apt => {
        const appointmentDate = new Date(apt.visitingDate || apt.appointmentDate);
        return appointmentDate <= new Date() || apt.status?.toLowerCase() === 'completed';
    });

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

    const getStatusColor = (status) => {
        switch (status?.toLowerCase()) {
            case 'confirmed':
            case 'scheduled': return 'bg-green-100 text-green-800';
            case 'pending': return 'bg-yellow-100 text-yellow-800';
            case 'cancelled': return 'bg-red-100 text-red-800';
            case 'completed': return 'bg-blue-100 text-blue-800';
            case 'in_progress': return 'bg-purple-100 text-purple-800';
            default: return 'bg-gray-100 text-gray-800';
        }
    };

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <Header title="My Appointments" showUserInfo={true} showLogout={true} />

            <main className="flex-1 container mx-auto px-4 py-8 max-w-7xl">
                <div className="space-y-6">
                    {/* Header */}
                    <div className="flex justify-between items-center">
                        <div>
                            <h2 className="text-2xl font-bold text-gray-900">My Appointments</h2>
                            <p className="text-gray-600">Manage your appointments with doctors</p>
                        </div>
                        <button className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors">
                            + Book New Appointment
                        </button>
                    </div>

                    {/* Search and Filter */}
                    <div className="bg-white p-6 rounded-lg shadow-sm border">
                        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                            <div className="md:col-span-2">
                                <label className="block text-sm font-medium text-gray-700 mb-2">Search Appointments</label>
                                <input
                                    type="text"
                                    placeholder="Search by doctor, problem description, or status..."
                                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent"
                                    value={searchTerm}
                                    onChange={(e) => setSearchTerm(e.target.value)}
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-2">Filter by Status</label>
                                <select
                                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent"
                                    value={statusFilter}
                                    onChange={(e) => setStatusFilter(e.target.value)}
                                >
                                    <option value="">All Statuses</option>
                                    {statuses.map((status, index) => (
                                        <option key={index} value={status}>{status}</option>
                                    ))}
                                </select>
                            </div>
                        </div>
                    </div>

                    {/* Stats */}
                    <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                        <div className="bg-white p-4 rounded-lg shadow-sm border">
                            <div className="flex items-center">
                                <div className="p-2 bg-blue-100 rounded-lg">
                                    <svg className="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                                    </svg>
                                </div>
                                <div className="ml-4">
                                    <p className="text-sm font-medium text-gray-600">Total</p>
                                    <p className="text-2xl font-bold text-gray-900">{patientAppointments.length}</p>
                                </div>
                            </div>
                        </div>

                        <div className="bg-white p-4 rounded-lg shadow-sm border">
                            <div className="flex items-center">
                                <div className="p-2 bg-green-100 rounded-lg">
                                    <svg className="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                                    </svg>
                                </div>
                                <div className="ml-4">
                                    <p className="text-sm font-medium text-gray-600">Upcoming</p>
                                    <p className="text-2xl font-bold text-gray-900">{upcomingAppointments.length}</p>
                                </div>
                            </div>
                        </div>

                        <div className="bg-white p-4 rounded-lg shadow-sm border">
                            <div className="flex items-center">
                                <div className="p-2 bg-purple-100 rounded-lg">
                                    <svg className="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                                    </svg>
                                </div>
                                <div className="ml-4">
                                    <p className="text-sm font-medium text-gray-600">Completed</p>
                                    <p className="text-2xl font-bold text-gray-900">{pastAppointments.length}</p>
                                </div>
                            </div>
                        </div>

                        <div className="bg-white p-4 rounded-lg shadow-sm border">
                            <div className="flex items-center">
                                <div className="p-2 bg-orange-100 rounded-lg">
                                    <svg className="w-6 h-6 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                                    </svg>
                                </div>
                                <div className="ml-4">
                                    <p className="text-sm font-medium text-gray-600">Found</p>
                                    <p className="text-2xl font-bold text-gray-900">{filteredAppointments.length}</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Upcoming Appointments */}
                    {upcomingAppointments.length > 0 && (
                        <div>
                            <h3 className="text-xl font-semibold text-gray-900 mb-4">Upcoming Appointments</h3>
                            <div className="space-y-4">
                                {upcomingAppointments.map((appointment) => (
                                    <div key={`upcoming-${appointment.id}`} className="bg-white p-6 rounded-lg shadow-sm border border-green-200">
                                        <div className="flex items-start justify-between">
                                            <div className="flex-1">
                                                <div className="flex items-center mb-2">
                                                    <h4 className="text-lg font-semibold text-gray-900">
                                                        Dr. {appointment.doctorName || 'Unknown Doctor'}
                                                    </h4>
                                                    <span className={`ml-3 px-2 py-1 text-xs font-medium rounded-full ${getStatusColor(appointment.status)}`}>
                                                        {appointment.status || 'Unknown'}
                                                    </span>
                                                </div>

                                                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
                                                    <div>
                                                        <p className="text-sm font-medium text-gray-700">Visiting Date:</p>
                                                        <p className="text-sm text-gray-600">
                                                            {appointment.visitingDate
                                                                ? new Date(appointment.visitingDate).toLocaleDateString('en-US', {
                                                                    weekday: 'long',
                                                                    year: 'numeric',
                                                                    month: 'long',
                                                                    day: 'numeric'
                                                                })
                                                                : 'Not specified'}
                                                        </p>
                                                    </div>
                                                    <div>
                                                        <p className="text-sm font-medium text-gray-700">Appointment ID:</p>
                                                        <p className="text-sm text-gray-600">#{appointment.id}</p>
                                                    </div>
                                                    <div>
                                                        <p className="text-sm font-medium text-gray-700">Serial Number:</p>
                                                        <p className="text-sm text-gray-600">{appointment.visitingSerialNumber || 'TBD'}</p>
                                                    </div>
                                                    <div className="md:col-span-2 lg:col-span-3">
                                                        <p className="text-sm font-medium text-gray-700">Problem Description:</p>
                                                        <p className="text-sm text-gray-600">{appointment.problemDescription || 'Not specified'}</p>
                                                    </div>
                                                    {appointment.bookingDate && (
                                                        <div>
                                                            <p className="text-sm font-medium text-gray-700">Booked On:</p>
                                                            <p className="text-sm text-gray-600">
                                                                {new Date(appointment.bookingDate).toLocaleDateString()}
                                                            </p>
                                                        </div>
                                                    )}
                                                </div>
                                            </div>
                                            <div className="ml-4 space-y-2">
                                                <button className="w-full px-3 py-1 text-sm bg-green-600 text-white rounded hover:bg-green-700 transition-colors">
                                                    View Details
                                                </button>
                                                <button className="w-full px-3 py-1 text-sm bg-red-600 text-white rounded hover:bg-red-700 transition-colors">
                                                    Cancel
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}

                    {/* Past Appointments */}
                    {pastAppointments.length > 0 && (
                        <div>
                            <h3 className="text-xl font-semibold text-gray-900 mb-4">Past Appointments</h3>
                            <div className="space-y-4">
                                {pastAppointments.map((appointment) => (
                                    <div key={`past-${appointment.id}`} className="bg-white p-6 rounded-lg shadow-sm border">
                                        <div className="flex items-start justify-between">
                                            <div className="flex-1">
                                                <div className="flex items-center mb-2">
                                                    <h4 className="text-lg font-semibold text-gray-900">
                                                        Dr. {appointment.doctorName || 'Unknown Doctor'}
                                                    </h4>
                                                    <span className={`ml-3 px-2 py-1 text-xs font-medium rounded-full ${getStatusColor(appointment.status)}`}>
                                                        {appointment.status || 'Unknown'}
                                                    </span>
                                                </div>

                                                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
                                                    <div>
                                                        <p className="text-sm font-medium text-gray-700">Visiting Date:</p>
                                                        <p className="text-sm text-gray-600">
                                                            {appointment.visitingDate
                                                                ? new Date(appointment.visitingDate).toLocaleDateString('en-US', {
                                                                    weekday: 'long',
                                                                    year: 'numeric',
                                                                    month: 'long',
                                                                    day: 'numeric'
                                                                })
                                                                : 'Not specified'}
                                                        </p>
                                                    </div>
                                                    <div>
                                                        <p className="text-sm font-medium text-gray-700">Appointment ID:</p>
                                                        <p className="text-sm text-gray-600">#{appointment.id}</p>
                                                    </div>
                                                    <div>
                                                        <p className="text-sm font-medium text-gray-700">Serial Number:</p>
                                                        <p className="text-sm text-gray-600">{appointment.visitingSerialNumber || 'TBD'}</p>
                                                    </div>
                                                    <div className="md:col-span-2 lg:col-span-3">
                                                        <p className="text-sm font-medium text-gray-700">Problem Description:</p>
                                                        <p className="text-sm text-gray-600">{appointment.problemDescription || 'Not specified'}</p>
                                                    </div>
                                                    {appointment.bookingDate && (
                                                        <div>
                                                            <p className="text-sm font-medium text-gray-700">Booked On:</p>
                                                            <p className="text-sm text-gray-600">
                                                                {new Date(appointment.bookingDate).toLocaleDateString()}
                                                            </p>
                                                        </div>
                                                    )}
                                                    {appointment.prescriptionId && (
                                                        <div>
                                                            <p className="text-sm font-medium text-gray-700">Prescription:</p>
                                                            <p className="text-sm text-gray-600">#{appointment.prescriptionId}</p>
                                                        </div>
                                                    )}
                                                </div>
                                            </div>
                                            <div className="ml-4">
                                                <button className="px-3 py-1 text-sm bg-gray-100 text-gray-700 rounded hover:bg-gray-200 transition-colors">
                                                    View Details
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}

                    {/* No Appointments */}
                    {filteredAppointments.length === 0 && (
                        <div className="text-center py-12">
                            <div className="text-gray-400 mb-4">
                                <svg className="w-16 h-16 mx-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                                </svg>
                            </div>
                            <p className="text-gray-500 mb-4">
                                {patientAppointments.length === 0
                                    ? "You don't have any appointments yet"
                                    : "No appointments match your search criteria"}
                            </p>
                            {searchTerm || statusFilter ? (
                                <button
                                    onClick={() => {
                                        setSearchTerm('');
                                        setStatusFilter('');
                                    }}
                                    className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700"
                                >
                                    Clear Filters
                                </button>
                            ) : (
                                <button className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700">
                                    Book Your First Appointment
                                </button>
                            )}
                        </div>
                    )}
                </div>
            </main>

            <Footer />
        </div>
    );
};

export default PatientAppointmentsPage;