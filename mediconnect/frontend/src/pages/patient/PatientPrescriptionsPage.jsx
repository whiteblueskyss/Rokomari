import React, { useState } from 'react';
import { usePrescriptions } from '../../hooks/useApi';
import { useAuth } from '../../contexts/AuthContext';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { LoadingSpinner, ErrorMessage } from '../../components/common';

const PatientPrescriptionsPage = () => {
    const { user } = useAuth();
    const { data: prescriptions, loading, error, refetch } = usePrescriptions();
    const [searchTerm, setSearchTerm] = useState('');

    // Filter prescriptions for current patient
    const patientPrescriptions = prescriptions.filter(prescription =>
        prescription.patientId === user?.id || prescription.patient?.id === user?.id
    );

    // Further filter based on search term
    const filteredPrescriptions = patientPrescriptions.filter(prescription =>
        prescription.medication?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        prescription.doctorName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        prescription.diagnosis?.toLowerCase().includes(searchTerm.toLowerCase())
    );

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex flex-col">
                <Header title="My Prescriptions" showUserInfo={true} showLogout={true} />
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
                <Header title="My Prescriptions" showUserInfo={true} showLogout={true} />
                <div className="flex-1 flex items-center justify-center">
                    <ErrorMessage message={error} />
                </div>
                <Footer />
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <Header title="My Prescriptions" showUserInfo={true} showLogout={true} />

            <main className="flex-1 container mx-auto px-4 py-8 max-w-7xl">
                <div className="space-y-6">
                    {/* Header */}
                    <div className="flex justify-between items-center">
                        <div>
                            <h2 className="text-2xl font-bold text-gray-900">My Prescriptions</h2>
                            <p className="text-gray-600">View and manage your prescriptions</p>
                        </div>
                    </div>

                    {/* Search */}
                    <div className="bg-white p-4 rounded-lg shadow-sm border">
                        <div className="flex items-center space-x-4">
                            <div className="flex-1">
                                <input
                                    type="text"
                                    placeholder="Search prescriptions by medication, doctor, or diagnosis..."
                                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent"
                                    value={searchTerm}
                                    onChange={(e) => setSearchTerm(e.target.value)}
                                />
                            </div>
                            <button
                                onClick={refetch}
                                className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
                            >
                                Refresh
                            </button>
                        </div>
                    </div>

                    {/* Stats */}
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                        <div className="bg-white p-4 rounded-lg shadow-sm border">
                            <div className="flex items-center">
                                <div className="p-2 bg-green-100 rounded-lg">
                                    <svg className="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                                    </svg>
                                </div>
                                <div className="ml-4">
                                    <p className="text-sm font-medium text-gray-600">Total Prescriptions</p>
                                    <p className="text-2xl font-bold text-gray-900">{patientPrescriptions.length}</p>
                                </div>
                            </div>
                        </div>

                        <div className="bg-white p-4 rounded-lg shadow-sm border">
                            <div className="flex items-center">
                                <div className="p-2 bg-blue-100 rounded-lg">
                                    <svg className="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                                    </svg>
                                </div>
                                <div className="ml-4">
                                    <p className="text-sm font-medium text-gray-600">Search Results</p>
                                    <p className="text-2xl font-bold text-gray-900">{filteredPrescriptions.length}</p>
                                </div>
                            </div>
                        </div>

                        <div className="bg-white p-4 rounded-lg shadow-sm border">
                            <div className="flex items-center">
                                <div className="p-2 bg-purple-100 rounded-lg">
                                    <svg className="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                                    </svg>
                                </div>
                                <div className="ml-4">
                                    <p className="text-sm font-medium text-gray-600">Recent</p>
                                    <p className="text-2xl font-bold text-gray-900">
                                        {patientPrescriptions.filter(p => {
                                            const date = new Date(p.prescriptionDate || p.createdAt);
                                            const monthAgo = new Date();
                                            monthAgo.setMonth(monthAgo.getMonth() - 1);
                                            return date > monthAgo;
                                        }).length}
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Prescriptions List */}
                    {filteredPrescriptions.length === 0 ? (
                        <div className="text-center py-12">
                            <div className="text-gray-400 mb-4">
                                <svg className="w-16 h-16 mx-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                                </svg>
                            </div>
                            <p className="text-gray-500 mb-4">
                                {patientPrescriptions.length === 0
                                    ? "You don't have any prescriptions yet"
                                    : "No prescriptions match your search criteria"}
                            </p>
                            {searchTerm && (
                                <button
                                    onClick={() => setSearchTerm('')}
                                    className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700"
                                >
                                    Clear Search
                                </button>
                            )}
                        </div>
                    ) : (
                        <div className="space-y-4">
                            {filteredPrescriptions.map((prescription) => (
                                <div key={prescription.id} className="bg-white p-6 rounded-lg shadow-sm border">
                                    <div className="flex items-start justify-between">
                                        <div className="flex-1">
                                            <div className="flex items-center mb-2">
                                                <h3 className="text-lg font-semibold text-gray-900">
                                                    {prescription.medication || 'Unknown Medication'}
                                                </h3>
                                                <span className="ml-3 px-2 py-1 bg-green-100 text-green-800 text-xs font-medium rounded-full">
                                                    Active
                                                </span>
                                            </div>

                                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
                                                <div>
                                                    <p className="text-sm font-medium text-gray-700">Doctor:</p>
                                                    <p className="text-sm text-gray-600">{prescription.doctorName || 'Not specified'}</p>
                                                </div>
                                                <div>
                                                    <p className="text-sm font-medium text-gray-700">Prescription Date:</p>
                                                    <p className="text-sm text-gray-600">
                                                        {prescription.prescriptionDate
                                                            ? new Date(prescription.prescriptionDate).toLocaleDateString()
                                                            : 'Not specified'}
                                                    </p>
                                                </div>
                                                <div>
                                                    <p className="text-sm font-medium text-gray-700">Dosage:</p>
                                                    <p className="text-sm text-gray-600">{prescription.dosage || 'Not specified'}</p>
                                                </div>
                                                <div>
                                                    <p className="text-sm font-medium text-gray-700">Duration:</p>
                                                    <p className="text-sm text-gray-600">{prescription.duration || 'Not specified'}</p>
                                                </div>
                                            </div>

                                            {prescription.diagnosis && (
                                                <div className="mt-4">
                                                    <p className="text-sm font-medium text-gray-700">Diagnosis:</p>
                                                    <p className="text-sm text-gray-600">{prescription.diagnosis}</p>
                                                </div>
                                            )}

                                            {prescription.instructions && (
                                                <div className="mt-4">
                                                    <p className="text-sm font-medium text-gray-700">Instructions:</p>
                                                    <p className="text-sm text-gray-600">{prescription.instructions}</p>
                                                </div>
                                            )}
                                        </div>

                                        <div className="ml-4">
                                            <button className="px-3 py-1 text-sm bg-gray-100 text-gray-700 rounded hover:bg-gray-200 transition-colors">
                                                Download
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </main>

            <Footer />
        </div>
    );
};

export default PatientPrescriptionsPage;