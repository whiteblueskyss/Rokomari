import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const AdminHome = () => {
    const navigate = useNavigate();
    const [stats, setStats] = useState({
        totalPatients: 0,
        totalDoctors: 0,
        totalAppointments: 0,
        pendingAppointments: 0
    });
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        fetchDashboardStats();
    }, []);

    const fetchDashboardStats = async () => {
        try {
            setIsLoading(true);
            // Fetch statistics from various endpoints
            const [patientsRes, doctorsRes, appointmentsRes] = await Promise.all([
                fetch('http://localhost:8080/api/patients', { credentials: 'include' }),
                fetch('http://localhost:8080/api/doctors', { credentials: 'include' }),
                fetch('http://localhost:8080/api/appointments', { credentials: 'include' })
            ]);

            const patients = await patientsRes.json();
            const doctors = await doctorsRes.json();
            const appointments = await appointmentsRes.json();

            setStats({
                totalPatients: patients.length || 0,
                totalDoctors: doctors.length || 0,
                totalAppointments: appointments.length || 0,
                pendingAppointments: appointments.filter(apt => apt.status === 'SCHEDULED').length || 0
            });
        } catch (error) {
            console.error('Error fetching dashboard stats:', error);
            // Set default values on error
            setStats({
                totalPatients: 0,
                totalDoctors: 0,
                totalAppointments: 0,
                pendingAppointments: 0
            });
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div>
            {/* Welcome Section */}
            <div className="mb-8">
                <h2 className="text-3xl font-bold text-gray-900 mb-2">Admin Dashboard</h2>
                <p className="text-gray-600">Manage the entire MediConnect system from here.</p>
            </div>
            {/* Stats Cards */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
                <div className="bg-white overflow-hidden shadow rounded-lg">
                    <div className="p-5">
                        <div className="flex items-center">
                            <div className="flex-shrink-0">
                                <div className="w-8 h-8 bg-blue-500 rounded-full flex items-center justify-center">
                                    <svg className="w-5 h-5 text-white" fill="currentColor" viewBox="0 0 20 20">
                                        <path fillRule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clipRule="evenodd" />
                                    </svg>
                                </div>
                            </div>
                            <div className="ml-5 w-0 flex-1">
                                <dl>
                                    <dt className="text-sm font-medium text-gray-500 truncate">Total Patients</dt>
                                    <dd className="text-lg font-medium text-gray-900">
                                        {isLoading ? '...' : stats.totalPatients}
                                    </dd>
                                </dl>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="bg-white overflow-hidden shadow rounded-lg">
                    <div className="p-5">
                        <div className="flex items-center">
                            <div className="flex-shrink-0">
                                <div className="w-8 h-8 bg-green-500 rounded-full flex items-center justify-center">
                                    <svg className="w-5 h-5 text-white" fill="currentColor" viewBox="0 0 20 20">
                                        <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                                    </svg>
                                </div>
                            </div>
                            <div className="ml-5 w-0 flex-1">
                                <dl>
                                    <dt className="text-sm font-medium text-gray-500 truncate">Total Doctors</dt>
                                    <dd className="text-lg font-medium text-gray-900">
                                        {isLoading ? '...' : stats.totalDoctors}
                                    </dd>
                                </dl>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="bg-white overflow-hidden shadow rounded-lg">
                    <div className="p-5">
                        <div className="flex items-center">
                            <div className="flex-shrink-0">
                                <div className="w-8 h-8 bg-yellow-500 rounded-full flex items-center justify-center">
                                    <svg className="w-5 h-5 text-white" fill="currentColor" viewBox="0 0 20 20">
                                        <path fillRule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clipRule="evenodd" />
                                    </svg>
                                </div>
                            </div>
                            <div className="ml-5 w-0 flex-1">
                                <dl>
                                    <dt className="text-sm font-medium text-gray-500 truncate">Total Appointments</dt>
                                    <dd className="text-lg font-medium text-gray-900">
                                        {isLoading ? '...' : stats.totalAppointments}
                                    </dd>
                                </dl>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="bg-white overflow-hidden shadow rounded-lg">
                    <div className="p-5">
                        <div className="flex items-center">
                            <div className="flex-shrink-0">
                                <div className="w-8 h-8 bg-purple-500 rounded-full flex items-center justify-center">
                                    <svg className="w-5 h-5 text-white" fill="currentColor" viewBox="0 0 20 20">
                                        <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                                    </svg>
                                </div>
                            </div>
                            <div className="ml-5 w-0 flex-1">
                                <dl>
                                    <dt className="text-sm font-medium text-gray-500 truncate">Pending Appointments</dt>
                                    <dd className="text-lg font-medium text-gray-900">
                                        {isLoading ? '...' : stats.pendingAppointments}
                                    </dd>
                                </dl>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Quick Actions */}
            <div className="bg-white shadow rounded-lg mb-8">
                <div className="px-4 py-5 sm:p-6">
                    <h3 className="text-lg leading-6 font-medium text-gray-900 mb-4">Quick Actions</h3>
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4">
                        <button
                            onClick={() => navigate('/admin-dashboard/patients')}
                            className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md text-sm font-medium"
                        >
                            Manage Patients
                        </button>
                        <button
                            onClick={() => navigate('/admin-dashboard/doctors')}
                            className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-md text-sm font-medium"
                        >
                            Manage Doctors
                        </button>
                        <button
                            onClick={() => navigate('/admin-dashboard/prescriptions')}
                            className="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-md text-sm font-medium"
                        >
                            Manage Prescriptions
                        </button>
                        <button
                            onClick={() => navigate('/admin-dashboard/appointments')}
                            className="bg-yellow-600 hover:bg-yellow-700 text-white px-4 py-2 rounded-md text-sm font-medium"
                        >
                            View Appointments
                        </button>
                        <button
                            onClick={() => navigate('/admin-dashboard/specializations')}
                            className="bg-purple-600 hover:bg-purple-700 text-white px-4 py-2 rounded-md text-sm font-medium"
                        >
                            Manage Specializations
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AdminHome;