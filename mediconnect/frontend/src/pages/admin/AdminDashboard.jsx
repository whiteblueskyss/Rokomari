import React from 'react';
import { useDoctors, usePatients, useAppointments, useSpecializations } from '../../hooks/useApi';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { LoadingSpinner, ErrorMessage } from '../../components/common';

const AdminDashboard = () => {
    const { data: doctors, loading: doctorsLoading } = useDoctors();
    const { data: patients, loading: patientsLoading } = usePatients();
    const { data: appointments, loading: appointmentsLoading } = useAppointments();
    const { data: specializations, loading: specializationsLoading } = useSpecializations();

    const statsCards = [
        {
            title: 'Total Doctors',
            count: doctorsLoading ? '...' : doctors.length,
            icon: '👨‍⚕️',
            color: 'blue',
            bgColor: 'bg-blue-100',
            textColor: 'text-blue-600'
        },
        {
            title: 'Total Patients',
            count: patientsLoading ? '...' : patients.length,
            icon: '👥',
            color: 'green',
            bgColor: 'bg-green-100',
            textColor: 'text-green-600'
        },
        {
            title: 'Total Appointments',
            count: appointmentsLoading ? '...' : appointments.length,
            icon: '📅',
            color: 'purple',
            bgColor: 'bg-purple-100',
            textColor: 'text-purple-600'
        },
        {
            title: 'Specializations',
            count: specializationsLoading ? '...' : specializations.length,
            icon: '🏥',
            color: 'orange',
            bgColor: 'bg-orange-100',
            textColor: 'text-orange-600'
        }
    ];

    const loading = doctorsLoading || patientsLoading || appointmentsLoading || specializationsLoading;

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex flex-col">
                <Header title="Admin Dashboard" showUserInfo={true} showLogout={true} />
                <div className="flex-1 flex items-center justify-center">
                    <LoadingSpinner />
                </div>
                <Footer />
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <Header title="Admin Dashboard" showUserInfo={true} showLogout={true} />

            <main className="flex-1 container mx-auto px-4 py-8 max-w-7xl">
                {/* Welcome Section */}
                <div className="mb-8">
                    <h2 className="text-3xl font-bold text-gray-900 mb-2">Admin Dashboard</h2>
                    <p className="text-gray-600">Manage the entire MediConnect system from here.</p>
                </div>

                {/* Quick Stats */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
                    {statsCards.map((stat, index) => (
                        <div key={index} className="bg-white p-6 rounded-lg shadow-sm border">
                            <div className="flex items-center">
                                <div className={`p-2 ${stat.bgColor} rounded-lg`}>
                                    <span className="text-2xl">{stat.icon}</span>
                                </div>
                                <div className="ml-4">
                                    <p className="text-sm font-medium text-gray-600">{stat.title}</p>
                                    <p className="text-2xl font-bold text-gray-900">{stat.count}</p>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>

                {/* Quick Actions */}
                <div className="bg-white rounded-lg shadow-sm border p-6">
                    <h3 className="text-lg font-semibold text-gray-900 mb-4">Quick Actions</h3>
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                        <button className="p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors text-left">
                            <div className="flex items-center space-x-3">
                                <span className="text-2xl">👨‍⚕️</span>
                                <div>
                                    <p className="font-medium text-gray-900">Add Doctor</p>
                                    <p className="text-sm text-gray-500">Register new doctor</p>
                                </div>
                            </div>
                        </button>

                        <button className="p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors text-left">
                            <div className="flex items-center space-x-3">
                                <span className="text-2xl">👥</span>
                                <div>
                                    <p className="font-medium text-gray-900">Add Patient</p>
                                    <p className="text-sm text-gray-500">Register new patient</p>
                                </div>
                            </div>
                        </button>

                        <button className="p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors text-left">
                            <div className="flex items-center space-x-3">
                                <span className="text-2xl">📅</span>
                                <div>
                                    <p className="font-medium text-gray-900">Schedule</p>
                                    <p className="text-sm text-gray-500">Book appointment</p>
                                </div>
                            </div>
                        </button>

                        <button className="p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors text-left">
                            <div className="flex items-center space-x-3">
                                <span className="text-2xl">🏥</span>
                                <div>
                                    <p className="font-medium text-gray-900">Specialization</p>
                                    <p className="text-sm text-gray-500">Manage departments</p>
                                </div>
                            </div>
                        </button>
                    </div>
                </div>

                {/* Recent Activity */}
                <div className="mt-8 bg-white rounded-lg shadow-sm border p-6">
                    <h3 className="text-lg font-semibold text-gray-900 mb-4">Recent Activity</h3>
                    <div className="space-y-4">
                        <div className="flex items-center space-x-3 p-3 bg-gray-50 rounded-lg">
                            <span className="text-green-500">✅</span>
                            <div>
                                <p className="text-sm font-medium text-gray-900">System is running smoothly</p>
                                <p className="text-xs text-gray-500">All services operational</p>
                            </div>
                        </div>
                        <div className="flex items-center space-x-3 p-3 bg-gray-50 rounded-lg">
                            <span className="text-blue-500">ℹ️</span>
                            <div>
                                <p className="text-sm font-medium text-gray-900">Database connected successfully</p>
                                <p className="text-xs text-gray-500">Backend services are online</p>
                            </div>
                        </div>
                    </div>
                </div>
            </main>

            <Footer />
        </div>
    );
};

export default AdminDashboard;