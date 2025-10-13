import React, { useState } from 'react';
import { useAppointments } from '../../hooks/useApi';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { LoadingSpinner, ErrorMessage } from '../../components/common';

const AppointmentsPage = () => {
    const { data: appointments, loading, error, refetch } = useAppointments();
    const [searchTerm, setSearchTerm] = useState('');
    const [statusFilter, setStatusFilter] = useState('all');

    // Filter appointments based on search term and status
    const filteredAppointments = appointments.filter(appointment => {
        const matchesSearch =
            appointment.patientName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
            appointment.doctorName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
            appointment.problemDescription?.toLowerCase().includes(searchTerm.toLowerCase());

        const matchesStatus = statusFilter === 'all' || appointment.status?.toLowerCase() === statusFilter.toLowerCase();

        return matchesSearch && matchesStatus;
    });

    const getStatusColor = (status) => {
        switch (status?.toLowerCase()) {
            case 'scheduled':
                return 'bg-blue-100 text-blue-800';
            case 'completed':
                return 'bg-green-100 text-green-800';
            case 'cancelled':
                return 'bg-red-100 text-red-800';
            case 'pending':
                return 'bg-yellow-100 text-yellow-800';
            default:
                return 'bg-gray-100 text-gray-800';
        }
    };

    const formatDate = (dateString) => {
        if (!dateString) return 'N/A';
        const date = new Date(dateString);
        return date.toLocaleDateString();
    };

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex flex-col">
                <Header title="Manage Appointments" showUserInfo={true} showLogout={true} />
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
                <Header title="Manage Appointments" showUserInfo={true} showLogout={true} />
                <div className="flex-1 flex items-center justify-center">
                    <ErrorMessage message={error} />
                </div>
                <Footer />
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <Header title="Manage Appointments" showUserInfo={true} showLogout={true} />

            <main className="flex-1 container mx-auto px-4 py-8 max-w-7xl">
                <div className="space-y-6">
                    {/* Header */}
                    <div className="flex justify-between items-center">
                        <div>
                            <h2 className="text-2xl font-bold text-gray-900">Appointments Management</h2>
                            <p className="text-gray-600">Manage all appointments in the system</p>
                        </div>
                        <button className="px-4 py-2 bg-purple-600 text-white rounded-lg hover:bg-purple-700 transition-colors">
                            + Schedule Appointment
                        </button>
                    </div>

                    {/* Search and Filters */}
                    <div className="bg-white p-4 rounded-lg shadow-sm border">
                        <div className="flex flex-col sm:flex-row gap-4">
                            <div className="flex-1">
                                <input
                                    type="text"
                                    placeholder="Search by patient name, doctor name, or problem description..."
                                    value={searchTerm}
                                    onChange={(e) => setSearchTerm(e.target.value)}
                                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-purple-500"
                                />
                            </div>
                            <div className="flex gap-2">
                                <select
                                    value={statusFilter}
                                    onChange={(e) => setStatusFilter(e.target.value)}
                                    className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-purple-500"
                                >
                                    <option value="all">All Status</option>
                                    <option value="scheduled">Scheduled</option>
                                    <option value="completed">Completed</option>
                                    <option value="cancelled">Cancelled</option>
                                    <option value="pending">Pending</option>
                                </select>
                                <button
                                    onClick={refetch}
                                    className="px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-colors"
                                >
                                    🔄 Refresh
                                </button>
                            </div>
                        </div>
                    </div>

                    {/* Stats */}
                    <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                        <div className="bg-white p-4 rounded-lg shadow-sm border">
                            <div className="flex items-center">
                                <span className="text-2xl mr-3">📅</span>
                                <div>
                                    <p className="text-sm text-gray-600">Total Appointments</p>
                                    <p className="text-xl font-bold text-gray-900">{appointments.length}</p>
                                </div>
                            </div>
                        </div>
                        <div className="bg-white p-4 rounded-lg shadow-sm border">
                            <div className="flex items-center">
                                <span className="text-2xl mr-3">🔍</span>
                                <div>
                                    <p className="text-sm text-gray-600">Filtered Results</p>
                                    <p className="text-xl font-bold text-gray-900">{filteredAppointments.length}</p>
                                </div>
                            </div>
                        </div>
                        <div className="bg-white p-4 rounded-lg shadow-sm border">
                            <div className="flex items-center">
                                <span className="text-2xl mr-3">⏰</span>
                                <div>
                                    <p className="text-sm text-gray-600">Today's Appointments</p>
                                    <p className="text-xl font-bold text-blue-600">
                                        {appointments.filter(apt => {
                                            const today = new Date().toDateString();
                                            const aptDate = new Date(apt.visitingDate).toDateString();
                                            return today === aptDate;
                                        }).length}
                                    </p>
                                </div>
                            </div>
                        </div>
                        <div className="bg-white p-4 rounded-lg shadow-sm border">
                            <div className="flex items-center">
                                <span className="text-2xl mr-3">✅</span>
                                <div>
                                    <p className="text-sm text-gray-600">Completed</p>
                                    <p className="text-xl font-bold text-green-600">
                                        {appointments.filter(apt => apt.status?.toLowerCase() === 'completed').length}
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Appointments Table */}
                    <div className="bg-white rounded-lg shadow-sm border overflow-hidden">
                        {filteredAppointments.length === 0 ? (
                            <div className="p-8 text-center">
                                <span className="text-4xl mb-4 block">📅</span>
                                <h3 className="text-lg font-medium text-gray-900 mb-2">
                                    {appointments.length === 0 ? 'No appointments found' : 'No matching appointments'}
                                </h3>
                                <p className="text-gray-500">
                                    {appointments.length === 0
                                        ? 'Start by scheduling your first appointment.'
                                        : 'Try adjusting your search criteria or filters.'
                                    }
                                </p>
                            </div>
                        ) : (
                            <div className="overflow-x-auto">
                                <table className="min-w-full divide-y divide-gray-200">
                                    <thead className="bg-gray-50">
                                        <tr>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Appointment ID
                                            </th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Patient
                                            </th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Doctor
                                            </th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Booking & Visit Date
                                            </th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Problem Description
                                            </th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Serial No
                                            </th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Status
                                            </th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Actions
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody className="bg-white divide-y divide-gray-200">
                                        {filteredAppointments.map((appointment) => {
                                            return (
                                                <tr key={appointment.id} className="hover:bg-gray-50">
                                                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                                        #{appointment.id}
                                                    </td>
                                                    <td className="px-6 py-4 whitespace-nowrap">
                                                        <div className="flex items-center">
                                                            <div className="h-8 w-8 rounded-full bg-green-100 flex items-center justify-center">
                                                                <span className="text-green-600 text-xs font-medium">
                                                                    {appointment.patientName?.split(' ').map(n => n[0]).join('')}
                                                                </span>
                                                            </div>
                                                            <div className="ml-3">
                                                                <div className="text-sm font-medium text-gray-900">
                                                                    {appointment.patientName}
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td className="px-6 py-4 whitespace-nowrap">
                                                        <div className="flex items-center">
                                                            <div className="h-8 w-8 rounded-full bg-blue-100 flex items-center justify-center">
                                                                <span className="text-blue-600 text-xs font-medium">
                                                                    {appointment.doctorName?.replace('Dr. ', '').split(' ').map(n => n[0]).join('')}
                                                                </span>
                                                            </div>
                                                            <div className="ml-3">
                                                                <div className="text-sm font-medium text-gray-900">
                                                                    {appointment.doctorName}
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                                        <div>Booked: {formatDate(appointment.bookingDate)}</div>
                                                        <div className="text-gray-500">Visit: {formatDate(appointment.visitingDate)}</div>
                                                    </td>
                                                    <td className="px-6 py-4 text-sm text-gray-900 max-w-xs truncate">
                                                        {appointment.problemDescription || 'General Consultation'}
                                                    </td>
                                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                                        #{appointment.visitingSerialNumber}
                                                    </td>
                                                    <td className="px-6 py-4 whitespace-nowrap">
                                                        <span className={`px-2 py-1 text-xs font-medium rounded-full ${getStatusColor(appointment.status)}`}>
                                                            {appointment.status || 'Pending'}
                                                        </span>
                                                    </td>
                                                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                                                        <button className="text-blue-600 hover:text-blue-900">
                                                            View
                                                        </button>
                                                        <button className="text-green-600 hover:text-green-900">
                                                            Edit
                                                        </button>
                                                        <button className="text-red-600 hover:text-red-900">
                                                            Cancel
                                                        </button>
                                                    </td>
                                                </tr>
                                            );
                                        })}
                                    </tbody>
                                </table>
                            </div>
                        )}
                    </div>
                </div>
            </main>

            <Footer />
        </div>
    );
};

export default AppointmentsPage;