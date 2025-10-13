import React from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { useNavigate, useLocation } from 'react-router-dom';

/**
 * Global Header Component
 * Customizable header that can be used across all pages
 */
const Header = ({
    title = "MediConnect",
    showUserInfo = true,
    showLogout = true,
    className = ""
}) => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();

    title = "MediConnect";

    const handleLogout = () => {
        console.log('Header: Logout clicked');
        logout();
    };

    // Admin navigation items
    const adminNavItems = [
        { name: 'Dashboard', path: '/admin-dashboard' },
        { name: 'Patients', path: '/admin-dashboard/patients' },
        { name: 'Doctors', path: '/admin-dashboard/doctors' },
        { name: 'Prescriptions', path: '/admin-dashboard/prescriptions' },
        { name: 'Appointments', path: '/admin-dashboard/appointments' },
        { name: 'Specializations', path: '/admin-dashboard/specializations' }
    ];

    // Patient navigation items
    const patientNavItems = [
        { name: 'Dashboard', path: '/patient' },
        { name: 'Doctors', path: '/patient/doctors' },
        { name: 'Prescriptions', path: '/patient/prescriptions' },
        { name: 'Appointments', path: '/patient/appointments' }
    ];

    // Doctor navigation items
    const doctorNavItems = [
        { name: 'Dashboard', path: '/doctor' },
        { name: 'Appointments', path: '/doctor/appointments' },
        { name: 'Patients', path: '/doctor/patients' },
        { name: 'Prescriptions', path: '/doctor/prescriptions' }
    ];

    const isAdmin = user?.userType === 'ADMIN' || user?.role === 'ADMIN';
    const isPatient = user?.userType === 'PATIENT' || user?.role === 'PATIENT';
    const isDoctor = user?.userType === 'DOCTOR' || user?.role === 'DOCTOR';

    return (
        <header className={`bg-white shadow-sm border-b ${className}`}>
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between items-center py-4">
                    {/* Left side - Title and User Info */}
                    <div>
                        <h1 className="text-2xl font-bold text-blue-600">{title}</h1>
                        {showUserInfo && user && (
                            <p className="text-sm text-gray-600 mt-1">
                                Welcome, {user.name || user.firstName || user.username || 'User'}
                            </p>
                        )}
                    </div>

                    {/* Center - Admin Navigation */}
                    {isAdmin && (
                        <nav className="flex items-center space-x-6">
                            {adminNavItems.map((item) => (
                                <button
                                    key={item.name}
                                    onClick={() => navigate(item.path)}
                                    className={`px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200 ${location.pathname === item.path
                                        ? 'bg-blue-100 text-blue-700'
                                        : 'text-gray-600 hover:text-blue-600 hover:bg-gray-100'
                                        }`}
                                >
                                    {item.name}
                                </button>
                            ))}
                        </nav>
                    )}

                    {/* Center - Patient Navigation */}
                    {isPatient && (
                        <nav className="flex items-center space-x-6">
                            {patientNavItems.map((item) => (
                                <button
                                    key={item.name}
                                    onClick={() => navigate(item.path)}
                                    className={`px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200 ${location.pathname === item.path
                                        ? 'bg-green-100 text-green-700'
                                        : 'text-gray-600 hover:text-green-600 hover:bg-gray-100'
                                        }`}
                                >
                                    {item.name}
                                </button>
                            ))}
                        </nav>
                    )}

                    {/* Center - Doctor Navigation */}
                    {isDoctor && (
                        <nav className="flex items-center space-x-6">
                            {doctorNavItems.map((item) => (
                                <button
                                    key={item.name}
                                    onClick={() => navigate(item.path)}
                                    className={`px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200 ${location.pathname === item.path
                                        ? 'bg-purple-100 text-purple-700'
                                        : 'text-gray-600 hover:text-purple-600 hover:bg-gray-100'
                                        }`}
                                >
                                    {item.name}
                                </button>
                            ))}
                        </nav>
                    )}

                    {/* Right side - Logout Button */}
                    {showLogout && user && (
                        <div className="flex items-center">
                            <button
                                onClick={handleLogout}
                                className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors duration-200 text-sm font-medium"
                            >
                                Logout
                            </button>
                        </div>
                    )}
                </div>
            </div>
        </header>
    );
};

export default Header;