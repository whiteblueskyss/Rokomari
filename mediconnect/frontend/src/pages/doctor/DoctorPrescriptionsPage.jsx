import React, { useState } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { usePrescriptions } from '../../hooks/useApi';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { LoadingSpinner, ErrorMessage } from '../../components/common';

const DoctorPrescriptionsPage = () => {
    const { user } = useAuth();
    const { data: prescriptions = [], loading, error } = usePrescriptions();
    const [searchTerm, setSearchTerm] = useState('');

    // Filter prescriptions for this doctor
    const doctorPrescriptions = prescriptions.filter(prescription =>
        prescription.doctorId === user?.id ||
        prescription.doctor?.id === user?.id
    );

    // Filter prescriptions based on search term
    const filteredPrescriptions = doctorPrescriptions.filter(prescription =>
        prescription.patient?.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        prescription.patientName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        prescription.medicationName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        prescription.medication?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        prescription.dosage?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        prescription.instructions?.toLowerCase().includes(searchTerm.toLowerCase())
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
                <div className="mb-8">
                    <h2 className="text-3xl font-bold text-gray-800 mb-2">My Prescriptions</h2>
                    <p className="text-gray-600">
                        View and manage prescriptions you have issued to patients
                    </p>
                </div>

                {/* Search and Stats */}
                <div className="mb-6 flex flex-col sm:flex-row sm:justify-between sm:items-center gap-4">
                    <div className="relative">
                        <svg className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                        </svg>
                        <input
                            type="text"
                            placeholder="Search prescriptions by patient, medication, or instructions..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            className="pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500 w-full sm:w-96"
                        />
                    </div>
                    <div className="text-sm text-gray-500">
                        Showing {filteredPrescriptions.length} of {doctorPrescriptions.length} prescriptions
                    </div>
                </div>

                {/* Prescriptions Grid */}
                {filteredPrescriptions.length === 0 ? (
                    <div className="bg-white rounded-lg shadow-md p-12 text-center">
                        <svg className="mx-auto h-12 w-12 text-gray-400 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                        </svg>
                        <h3 className="text-lg font-medium text-gray-900 mb-2">No prescriptions found</h3>
                        <p className="text-gray-500">
                            {searchTerm
                                ? `No prescriptions match your search term "${searchTerm}".`
                                : 'You haven\'t issued any prescriptions yet.'
                            }
                        </p>
                    </div>
                ) : (
                    <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
                        {filteredPrescriptions.map((prescription) => (
                            <div key={prescription.id} className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
                                <div className="flex items-start justify-between mb-4">
                                    <div className="flex items-center">
                                        <div className="p-3 bg-purple-100 rounded-full mr-3">
                                            <svg className="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                                            </svg>
                                        </div>
                                        <div>
                                            <h3 className="font-semibold text-gray-900">
                                                {prescription.patient?.name || prescription.patientName || 'Unknown Patient'}
                                            </h3>
                                            <p className="text-sm text-gray-600">
                                                Patient ID: {prescription.patient?.id || prescription.patientId || 'N/A'}
                                            </p>
                                        </div>
                                    </div>
                                    <div className="text-right">
                                        <p className="text-xs text-gray-500">
                                            {new Date(prescription.createdDate || prescription.dateCreated || prescription.prescriptionDate).toLocaleDateString()}
                                        </p>
                                    </div>
                                </div>

                                <div className="space-y-4">
                                    {/* Medication Information */}
                                    <div className="bg-gray-50 rounded-lg p-4">
                                        <h4 className="font-medium text-gray-900 mb-2">Medication</h4>
                                        <div className="space-y-2">
                                            <div>
                                                <span className="text-sm font-medium text-gray-700">Name: </span>
                                                <span className="text-sm text-gray-900">
                                                    {prescription.medicationName || prescription.medication || 'Not specified'}
                                                </span>
                                            </div>
                                            <div>
                                                <span className="text-sm font-medium text-gray-700">Dosage: </span>
                                                <span className="text-sm text-gray-900">
                                                    {prescription.dosage || 'Not specified'}
                                                </span>
                                            </div>
                                            {prescription.frequency && (
                                                <div>
                                                    <span className="text-sm font-medium text-gray-700">Frequency: </span>
                                                    <span className="text-sm text-gray-900">{prescription.frequency}</span>
                                                </div>
                                            )}
                                            {prescription.duration && (
                                                <div>
                                                    <span className="text-sm font-medium text-gray-700">Duration: </span>
                                                    <span className="text-sm text-gray-900">{prescription.duration}</span>
                                                </div>
                                            )}
                                        </div>
                                    </div>

                                    {/* Instructions */}
                                    {prescription.instructions && (
                                        <div>
                                            <h4 className="font-medium text-gray-900 mb-2">Instructions</h4>
                                            <p className="text-sm text-gray-700 bg-blue-50 p-3 rounded-lg">
                                                {prescription.instructions}
                                            </p>
                                        </div>
                                    )}

                                    {/* Additional Information */}
                                    <div className="space-y-2">
                                        {prescription.diagnosis && (
                                            <div>
                                                <span className="text-sm font-medium text-gray-700">Diagnosis: </span>
                                                <span className="text-sm text-gray-900">{prescription.diagnosis}</span>
                                            </div>
                                        )}

                                        {prescription.notes && (
                                            <div>
                                                <span className="text-sm font-medium text-gray-700">Notes: </span>
                                                <span className="text-sm text-gray-900">{prescription.notes}</span>
                                            </div>
                                        )}
                                    </div>
                                </div>

                                <div className="mt-4 pt-4 border-t border-gray-200">
                                    <div className="flex justify-between items-center text-xs text-gray-500">
                                        <span>Prescription ID: {prescription.id}</span>
                                        {prescription.status && (
                                            <span className={`px-2 py-1 rounded-full text-xs font-medium ${prescription.status === 'ACTIVE' ? 'bg-green-100 text-green-800' :
                                                    prescription.status === 'EXPIRED' ? 'bg-red-100 text-red-800' :
                                                        'bg-gray-100 text-gray-800'
                                                }`}>
                                                {prescription.status}
                                            </span>
                                        )}
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                )}

                {/* Summary Statistics */}
                {doctorPrescriptions.length > 0 && (
                    <div className="mt-8 bg-white rounded-lg shadow-md p-6">
                        <h3 className="text-lg font-semibold text-gray-800 mb-4">Prescription Overview</h3>
                        <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
                            <div className="text-center">
                                <div className="p-3 bg-purple-100 rounded-full inline-flex mb-2">
                                    <svg className="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                                    </svg>
                                </div>
                                <p className="text-2xl font-bold text-gray-900">{doctorPrescriptions.length}</p>
                                <p className="text-sm text-gray-600">Total Prescriptions</p>
                            </div>

                            <div className="text-center">
                                <div className="p-3 bg-green-100 rounded-full inline-flex mb-2">
                                    <svg className="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                                    </svg>
                                </div>
                                <p className="text-2xl font-bold text-gray-900">
                                    {doctorPrescriptions.filter(p => p.status === 'ACTIVE').length}
                                </p>
                                <p className="text-sm text-gray-600">Active</p>
                            </div>

                            <div className="text-center">
                                <div className="p-3 bg-blue-100 rounded-full inline-flex mb-2">
                                    <svg className="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                                    </svg>
                                </div>
                                <p className="text-2xl font-bold text-gray-900">
                                    {doctorPrescriptions.filter(p => {
                                        const date = new Date(p.createdDate || p.dateCreated || p.prescriptionDate);
                                        const today = new Date();
                                        return date.toDateString() === today.toDateString();
                                    }).length}
                                </p>
                                <p className="text-sm text-gray-600">Today</p>
                            </div>

                            <div className="text-center">
                                <div className="p-3 bg-yellow-100 rounded-full inline-flex mb-2">
                                    <svg className="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                                    </svg>
                                </div>
                                <p className="text-2xl font-bold text-gray-900">
                                    {[...new Set(doctorPrescriptions.map(p => p.patientId || p.patient?.id).filter(Boolean))].length}
                                </p>
                                <p className="text-sm text-gray-600">Unique Patients</p>
                            </div>
                        </div>
                    </div>
                )}
            </main>

            <Footer />
        </div>
    );
};

export default DoctorPrescriptionsPage;