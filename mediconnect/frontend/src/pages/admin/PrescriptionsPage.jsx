import React, { useState } from 'react';
import { usePrescriptions } from '../../hooks/useApi';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { LoadingSpinner, ErrorMessage } from '../../components/common';

const PrescriptionsPage = () => {
    const { data: prescriptions = [], loading, error, refetch } = usePrescriptions();
    const [searchTerm, setSearchTerm] = useState('');
    const [statusFilter, setStatusFilter] = useState('all');

    // Filter prescriptions based on search term and status
    const filteredPrescriptions = prescriptions.filter(prescription => {
        const matchesSearch =
            prescription.patientName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
            prescription.doctorName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
            prescription.problem?.toLowerCase().includes(searchTerm.toLowerCase());

        const matchesStatus = statusFilter === 'all' || prescription.status?.toLowerCase() === statusFilter.toLowerCase();

        return matchesSearch && matchesStatus;
    });

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex flex-col">
                <Header title="Manage Prescriptions" showUserInfo={true} showLogout={true} />
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
                <Header title="Manage Prescriptions" showUserInfo={true} showLogout={true} />
                <div className="flex-1 flex items-center justify-center">
                    <ErrorMessage message={error} />
                </div>
                <Footer />
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <Header title="Manage Prescriptions" showUserInfo={true} showLogout={true} />

            <main className="flex-1 container mx-auto px-4 py-8 max-w-7xl">
                <div className="space-y-6">
                    {/* Header */}
                    <div className="flex justify-between items-center">
                        <div>
                            <h2 className="text-2xl font-bold text-gray-900">Prescriptions Management</h2>
                            <p className="text-gray-600">View and manage all patient prescriptions</p>
                        </div>
                        <button
                            onClick={refetch}
                            className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
                        >
                            🔄 Refresh
                        </button>
                    </div>

                    {/* Search and Filters */}
                    <div className="bg-white p-4 rounded-lg shadow-sm border">
                        <div className="flex flex-col sm:flex-row gap-4">
                            <div className="flex-1">
                                <input
                                    type="text"
                                    placeholder="Search by patient name, doctor name, or problem..."
                                    value={searchTerm}
                                    onChange={(e) => setSearchTerm(e.target.value)}
                                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                                />
                            </div>
                            <div>
                                <select
                                    value={statusFilter}
                                    onChange={(e) => setStatusFilter(e.target.value)}
                                    className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                                >
                                    <option value="all">All Status</option>
                                    <option value="active">Active</option>
                                    <option value="completed">Completed</option>
                                    <option value="cancelled">Cancelled</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    {/* Stats */}
                    <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                        <div className="bg-white p-4 rounded-lg shadow-sm border">
                            <div className="flex items-center">
                                <span className="text-2xl mr-3">📋</span>
                                <div>
                                    <p className="text-sm text-gray-600">Total Prescriptions</p>
                                    <p className="text-xl font-bold text-gray-900">{prescriptions.length}</p>
                                </div>
                            </div>
                        </div>
                        <div className="bg-white p-4 rounded-lg shadow-sm border">
                            <div className="flex items-center">
                                <span className="text-2xl mr-3">✅</span>
                                <div>
                                    <p className="text-sm text-gray-600">Active</p>
                                    <p className="text-xl font-bold text-green-600">
                                        {prescriptions.filter(p => p.status === 'ACTIVE').length}
                                    </p>
                                </div>
                            </div>
                        </div>
                        <div className="bg-white p-4 rounded-lg shadow-sm border">
                            <div className="flex items-center">
                                <span className="text-2xl mr-3">🔍</span>
                                <div>
                                    <p className="text-sm text-gray-600">Search Results</p>
                                    <p className="text-xl font-bold text-gray-900">{filteredPrescriptions.length}</p>
                                </div>
                            </div>
                        </div>
                        <div className="bg-white p-4 rounded-lg shadow-sm border">
                            <div className="flex items-center">
                                <span className="text-2xl mr-3">📅</span>
                                <div>
                                    <p className="text-sm text-gray-600">This Month</p>
                                    <p className="text-xl font-bold text-blue-600">
                                        {prescriptions.filter(p => {
                                            const prescriptionDate = new Date(p.prescriptionDate);
                                            const now = new Date();
                                            return prescriptionDate.getMonth() === now.getMonth() &&
                                                prescriptionDate.getFullYear() === now.getFullYear();
                                        }).length}
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Prescriptions List */}
                    <div className="bg-white rounded-lg shadow-sm border">
                        {filteredPrescriptions.length === 0 ? (
                            <div className="text-center py-12">
                                <span className="text-4xl mb-4 block">📋</span>
                                <h3 className="text-lg font-medium text-gray-900 mb-2">
                                    {prescriptions.length === 0 ? 'No prescriptions found' : 'No matching prescriptions'}
                                </h3>
                                <p className="text-gray-500">
                                    {prescriptions.length === 0
                                        ? 'Prescriptions will appear here once doctors start creating them.'
                                        : 'Try adjusting your search criteria or filters.'
                                    }
                                </p>
                            </div>
                        ) : (
                            <div className="divide-y divide-gray-200">
                                {filteredPrescriptions.map((prescription) => (
                                    <div key={prescription.id} className="p-6 hover:bg-gray-50 transition-colors">
                                        <div className="flex items-start justify-between mb-4">
                                            <div className="flex items-start space-x-4">
                                                <div className="h-12 w-12 rounded-full bg-blue-100 flex items-center justify-center">
                                                    <span className="text-blue-600 text-xl">📋</span>
                                                </div>
                                                <div>
                                                    <h3 className="text-lg font-semibold text-gray-900">
                                                        Prescription #{prescription.id}
                                                    </h3>
                                                    <p className="text-sm text-gray-600">
                                                        Date: {new Date(prescription.prescriptionDate).toLocaleDateString()}
                                                    </p>
                                                </div>
                                            </div>
                                            <span className={`px-3 py-1 text-xs font-medium rounded-full ${prescription.status === 'ACTIVE'
                                                    ? 'bg-green-100 text-green-800'
                                                    : prescription.status === 'COMPLETED'
                                                        ? 'bg-blue-100 text-blue-800'
                                                        : 'bg-gray-100 text-gray-800'
                                                }`}>
                                                {prescription.status}
                                            </span>
                                        </div>

                                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                                            <div>
                                                <h4 className="font-medium text-gray-900 mb-2">Patient Information</h4>
                                                <p className="text-sm text-gray-600">
                                                    <span className="font-medium">Name:</span> {prescription.patientName}
                                                </p>
                                                <p className="text-sm text-gray-600">
                                                    <span className="font-medium">ID:</span> {prescription.patientId}
                                                </p>
                                            </div>
                                            <div>
                                                <h4 className="font-medium text-gray-900 mb-2">Doctor Information</h4>
                                                <p className="text-sm text-gray-600">
                                                    <span className="font-medium">Name:</span> {prescription.doctorName}
                                                </p>
                                                <p className="text-sm text-gray-600">
                                                    <span className="font-medium">ID:</span> {prescription.doctorId}
                                                </p>
                                            </div>
                                        </div>

                                        <div className="mb-4">
                                            <h4 className="font-medium text-gray-900 mb-2">Problem</h4>
                                            <p className="text-sm text-gray-600 bg-gray-50 p-3 rounded-lg">
                                                {prescription.problem}
                                            </p>
                                        </div>

                                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-4">
                                            {prescription.tests && prescription.tests.length > 0 && (
                                                <div>
                                                    <h4 className="font-medium text-gray-900 mb-2">Tests</h4>
                                                    <ul className="text-sm text-gray-600 space-y-1">
                                                        {prescription.tests.map((test, index) => (
                                                            <li key={index} className="flex items-center">
                                                                <span className="w-1.5 h-1.5 bg-blue-500 rounded-full mr-2"></span>
                                                                {test}
                                                            </li>
                                                        ))}
                                                    </ul>
                                                </div>
                                            )}

                                            {prescription.tablets && prescription.tablets.length > 0 && (
                                                <div>
                                                    <h4 className="font-medium text-gray-900 mb-2">Tablets</h4>
                                                    <ul className="text-sm text-gray-600 space-y-1">
                                                        {prescription.tablets.map((tablet, index) => (
                                                            <li key={index} className="flex items-center">
                                                                <span className="w-1.5 h-1.5 bg-green-500 rounded-full mr-2"></span>
                                                                {tablet}
                                                            </li>
                                                        ))}
                                                    </ul>
                                                </div>
                                            )}

                                            {prescription.capsules && prescription.capsules.length > 0 && (
                                                <div>
                                                    <h4 className="font-medium text-gray-900 mb-2">Capsules</h4>
                                                    <ul className="text-sm text-gray-600 space-y-1">
                                                        {prescription.capsules.map((capsule, index) => (
                                                            <li key={index} className="flex items-center">
                                                                <span className="w-1.5 h-1.5 bg-orange-500 rounded-full mr-2"></span>
                                                                {capsule}
                                                            </li>
                                                        ))}
                                                    </ul>
                                                </div>
                                            )}

                                            {prescription.vaccines && prescription.vaccines.length > 0 && (
                                                <div>
                                                    <h4 className="font-medium text-gray-900 mb-2">Vaccines</h4>
                                                    <ul className="text-sm text-gray-600 space-y-1">
                                                        {prescription.vaccines.map((vaccine, index) => (
                                                            <li key={index} className="flex items-center">
                                                                <span className="w-1.5 h-1.5 bg-purple-500 rounded-full mr-2"></span>
                                                                {vaccine}
                                                            </li>
                                                        ))}
                                                    </ul>
                                                </div>
                                            )}
                                        </div>

                                        {prescription.advice && (
                                            <div className="mb-4">
                                                <h4 className="font-medium text-gray-900 mb-2">Doctor's Advice</h4>
                                                <p className="text-sm text-gray-600 bg-yellow-50 p-3 rounded-lg border-l-4 border-yellow-400">
                                                    {prescription.advice}
                                                </p>
                                            </div>
                                        )}

                                        {prescription.other && (
                                            <div className="mb-4">
                                                <h4 className="font-medium text-gray-900 mb-2">Additional Notes</h4>
                                                <p className="text-sm text-gray-600 bg-gray-50 p-3 rounded-lg">
                                                    {prescription.other}
                                                </p>
                                            </div>
                                        )}

                                        <div className="flex items-center justify-between pt-4 border-t border-gray-200">
                                            <div className="text-sm text-gray-500">
                                                {prescription.followUpDate && (
                                                    <span>
                                                        <span className="font-medium">Follow-up:</span> {' '}
                                                        {new Date(prescription.followUpDate).toLocaleDateString()}
                                                    </span>
                                                )}
                                            </div>
                                            <div className="flex space-x-2">
                                                <button className="px-3 py-1 text-xs bg-blue-100 text-blue-700 rounded-full hover:bg-blue-200 transition-colors">
                                                    View Details
                                                </button>
                                                <button className="px-3 py-1 text-xs bg-green-100 text-green-700 rounded-full hover:bg-green-200 transition-colors">
                                                    Print
                                                </button>
                                                <button className="px-3 py-1 text-xs bg-gray-100 text-gray-700 rounded-full hover:bg-gray-200 transition-colors">
                                                    Edit
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>
                </div>
            </main>

            <Footer />
        </div>
    );
};

export default PrescriptionsPage;
