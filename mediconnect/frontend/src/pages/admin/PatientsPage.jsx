import React, { useState } from 'react';
import { usePatients } from '../../hooks/useApi';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { LoadingSpinner, ErrorMessage } from '../../components/common';

const PatientsPage = () => {
    const { data: patients, loading, error, refetch } = usePatients();
    const [searchTerm, setSearchTerm] = useState('');

    // Filter patients based on search term
    const filteredPatients = patients.filter(patient =>
        patient.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        patient.email?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        patient.phone?.includes(searchTerm)
    );

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex flex-col">
                <Header title="Manage Patients" showUserInfo={true} showLogout={true} />
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
                <Header title="Manage Patients" showUserInfo={true} showLogout={true} />
                <div className="flex-1 flex items-center justify-center">
                    <ErrorMessage message={error} />
                </div>
                <Footer />
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <Header title="Manage Patients" showUserInfo={true} showLogout={true} />

            <main className="flex-1 container mx-auto px-4 py-8 max-w-7xl">
                <div className="space-y-6">
                    {/* Header */}
                    <div className="flex justify-between items-center">
                        <div>
                            <h2 className="text-2xl font-bold text-gray-900">Patients Management</h2>
                            <p className="text-gray-600">Manage all registered patients</p>
                        </div>
                        <button className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors">
                            + Add New Patient
                        </button>
                    </div>

                    {/* Search and Filters */}
                    <div className="bg-white p-4 rounded-lg shadow-sm border">
                        <div className="flex flex-col sm:flex-row gap-4">
                            <div className="flex-1">
                                <input
                                    type="text"
                                    placeholder="Search patients by name, email, or phone number..."
                                    value={searchTerm}
                                    onChange={(e) => setSearchTerm(e.target.value)}
                                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500"
                                />
                            </div>
                            <button
                                onClick={refetch}
                                className="px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-colors"
                            >
                                🔄 Refresh
                            </button>
                        </div>
                    </div>

                    {/* Stats */}
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                        <div className="bg-white p-4 rounded-lg shadow-sm border">
                            <div className="flex items-center">
                                <span className="text-2xl mr-3">👥</span>
                                <div>
                                    <p className="text-sm text-gray-600">Total Patients</p>
                                    <p className="text-xl font-bold text-gray-900">{patients.length}</p>
                                </div>
                            </div>
                        </div>
                        <div className="bg-white p-4 rounded-lg shadow-sm border">
                            <div className="flex items-center">
                                <span className="text-2xl mr-3">🔍</span>
                                <div>
                                    <p className="text-sm text-gray-600">Search Results</p>
                                    <p className="text-xl font-bold text-gray-900">{filteredPatients.length}</p>
                                </div>
                            </div>
                        </div>
                        <div className="bg-white p-4 rounded-lg shadow-sm border">
                            <div className="flex items-center">
                                <span className="text-2xl mr-3">✅</span>
                                <div>
                                    <p className="text-sm text-gray-600">Active Status</p>
                                    <p className="text-xl font-bold text-green-600">All Active</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Patients Table */}
                    <div className="bg-white rounded-lg shadow-sm border overflow-hidden">
                        {filteredPatients.length === 0 ? (
                            <div className="p-8 text-center">
                                <span className="text-4xl mb-4 block">👥</span>
                                <h3 className="text-lg font-medium text-gray-900 mb-2">
                                    {patients.length === 0 ? 'No patients found' : 'No matching patients'}
                                </h3>
                                <p className="text-gray-500">
                                    {patients.length === 0
                                        ? 'Start by adding your first patient to the system.'
                                        : 'Try adjusting your search criteria.'
                                    }
                                </p>
                            </div>
                        ) : (
                            <div className="overflow-x-auto">
                                <table className="min-w-full divide-y divide-gray-200">
                                    <thead className="bg-gray-50">
                                        <tr>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Patient
                                            </th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Contact Info
                                            </th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Personal Info
                                            </th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Address
                                            </th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Actions
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody className="bg-white divide-y divide-gray-200">
                                        {filteredPatients.map((patient) => (
                                            <tr key={patient.id} className="hover:bg-gray-50">
                                                <td className="px-6 py-4 whitespace-nowrap">
                                                    <div className="flex items-center">
                                                        <div className="h-10 w-10 rounded-full bg-green-100 flex items-center justify-center">
                                                            <span className="text-green-600 font-medium">
                                                                {patient.name?.split(' ').map(n => n[0]).join('')}
                                                            </span>
                                                        </div>
                                                        <div className="ml-4">
                                                            <div className="text-sm font-medium text-gray-900">
                                                                {patient.name}
                                                            </div>
                                                            <div className="text-sm text-gray-500">
                                                                ID: {patient.id}
                                                            </div>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                                    <div>{patient.email}</div>
                                                    <div className="text-gray-500">{patient.phone}</div>
                                                </td>
                                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                                    <div>
                                                        <span className="text-gray-500">Age: </span>
                                                        {patient.age || 'N/A'}
                                                    </div>
                                                    <div>
                                                        <span className="text-gray-500">Gender: </span>
                                                        {patient.gender || 'N/A'}
                                                    </div>
                                                </td>
                                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                    <div className="text-xs truncate max-w-xs">
                                                        {patient.address || 'N/A'}
                                                    </div>
                                                </td>
                                                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                                                    <button className="text-blue-600 hover:text-blue-900">
                                                        View
                                                    </button>
                                                    <button className="text-green-600 hover:text-green-900">
                                                        Edit
                                                    </button>
                                                    <button className="text-red-600 hover:text-red-900">
                                                        Delete
                                                    </button>
                                                </td>
                                            </tr>
                                        ))}
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

export default PatientsPage;