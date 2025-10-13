import React, { useState } from 'react';
import { useSpecializations } from '../../hooks/useApi';
import { specializationService } from '../../services/api';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { LoadingSpinner, ErrorMessage } from '../../components/common';
import toast, { Toaster } from 'react-hot-toast';

const SpecializationsPage = () => {
    const { data: specializations, loading, error, refetch } = useSpecializations();
    const [searchTerm, setSearchTerm] = useState('');
    const [showModal, setShowModal] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [formData, setFormData] = useState({
        name: '',
        description: ''
    });

    // Filter specializations based on search term
    const filteredSpecializations = specializations.filter(specialization =>
        specialization.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        specialization.description?.toLowerCase().includes(searchTerm.toLowerCase())
    );

    // Handle form input changes
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    // Handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!formData.name.trim()) {
            toast.error('Specialization name is required');
            return;
        }

        setIsSubmitting(true);
        try {
            await specializationService.createSpecialization(formData);
            setShowModal(false);
            setFormData({ name: '', description: '' });
            refetch(); // Refresh the list
            toast.success('Specialization added successfully!');
        } catch (error) {
            console.error('Error creating specialization:', error);
            toast.error('Failed to create specialization. Please try again.');
        } finally {
            setIsSubmitting(false);
        }
    };

    // Handle modal close
    const handleModalClose = () => {
        setShowModal(false);
        setFormData({ name: '', description: '' });
    };

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex flex-col">
                <Header title="Manage Specializations" showUserInfo={true} showLogout={true} />
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
                <Header title="Manage Specializations" showUserInfo={true} showLogout={true} />
                <div className="flex-1 flex items-center justify-center">
                    <ErrorMessage message={error} />
                </div>
                <Footer />
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <Header title="Manage Specializations" showUserInfo={true} showLogout={true} />

            <main className="flex-1 container mx-auto px-4 py-8 max-w-7xl">
                <div className="space-y-6">
                    {/* Header */}
                    <div className="flex justify-between items-center">
                        <div>
                            <h2 className="text-2xl font-bold text-gray-900">Specializations Management</h2>
                            <p className="text-gray-600">Manage medical specializations and departments</p>
                        </div>
                        <button
                            onClick={() => setShowModal(true)}
                            className="px-4 py-2 bg-orange-600 text-white rounded-lg hover:bg-orange-700 transition-colors"
                        >
                            + Add New Specialization
                        </button>
                    </div>

                    {/* Search and Filters */}
                    <div className="bg-white p-4 rounded-lg shadow-sm border">
                        <div className="flex flex-col sm:flex-row gap-4">
                            <div className="flex-1">
                                <input
                                    type="text"
                                    placeholder="Search specializations by name or description..."
                                    value={searchTerm}
                                    onChange={(e) => setSearchTerm(e.target.value)}
                                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
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
                                <span className="text-2xl mr-3">🏥</span>
                                <div>
                                    <p className="text-sm text-gray-600">Total Specializations</p>
                                    <p className="text-xl font-bold text-gray-900">{specializations.length}</p>
                                </div>
                            </div>
                        </div>
                        <div className="bg-white p-4 rounded-lg shadow-sm border">
                            <div className="flex items-center">
                                <span className="text-2xl mr-3">🔍</span>
                                <div>
                                    <p className="text-sm text-gray-600">Search Results</p>
                                    <p className="text-xl font-bold text-gray-900">{filteredSpecializations.length}</p>
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


                    {/* Summary Table */}
                    {filteredSpecializations.length > 0 && (
                        <div className="bg-white rounded-lg shadow-sm border overflow-hidden">
                            <div className="px-6 py-4 border-b border-gray-200">
                                <h3 className="text-lg font-medium text-gray-900">Specializations Summary</h3>
                            </div>
                            <div className="overflow-x-auto">
                                <table className="min-w-full divide-y divide-gray-200">
                                    <thead className="bg-gray-50">
                                        <tr>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Specialization
                                            </th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Description
                                            </th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Doctors Count
                                            </th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Actions
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody className="bg-white divide-y divide-gray-200">
                                        {filteredSpecializations.map((specialization) => (
                                            <tr key={specialization.id} className="hover:bg-gray-50">
                                                <td className="px-6 py-4 whitespace-nowrap">
                                                    <div className="flex items-center">
                                                        <div className="h-8 w-8 rounded-full bg-orange-100 flex items-center justify-center">
                                                            <span className="text-orange-600 text-sm">🏥</span>
                                                        </div>
                                                        <div className="ml-3">
                                                            <div className="text-sm font-medium text-gray-900">
                                                                {specialization.name}
                                                            </div>
                                                            <div className="text-sm text-gray-500">ID: {specialization.id}</div>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td className="px-6 py-4 text-sm text-gray-900 max-w-xs">
                                                    <div className="truncate">
                                                        {specialization.description || 'No description available'}
                                                    </div>
                                                </td>
                                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                                    <span className="px-2 py-1 bg-blue-100 text-blue-800 text-xs font-medium rounded-full">
                                                        {specialization.doctorCount || 0} doctors
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
                                                        Delete
                                                    </button>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    )}


                    {/* Specializations Grid */}
                    <div className="bg-white rounded-lg shadow-sm border p-6">
                        {filteredSpecializations.length === 0 ? (
                            <div className="text-center py-12">
                                <span className="text-4xl mb-4 block">🏥</span>
                                <h3 className="text-lg font-medium text-gray-900 mb-2">
                                    {specializations.length === 0 ? 'No specializations found' : 'No matching specializations'}
                                </h3>
                                <p className="text-gray-500">
                                    {specializations.length === 0
                                        ? 'Start by adding your first medical specialization.'
                                        : 'Try adjusting your search criteria.'
                                    }
                                </p>
                            </div>
                        ) : (
                            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                                {filteredSpecializations.map((specialization) => (
                                    <div key={specialization.id} className="border border-gray-200 rounded-lg p-6 hover:shadow-md transition-shadow">
                                        <div className="flex items-start justify-between mb-4">
                                            <div className="flex items-center">
                                                <div className="h-12 w-12 rounded-lg bg-orange-100 flex items-center justify-center">
                                                    <span className="text-orange-600 text-xl">🏥</span>
                                                </div>
                                                <div className="ml-3">
                                                    <h3 className="text-lg font-semibold text-gray-900">
                                                        {specialization.name}
                                                    </h3>
                                                    <p className="text-sm text-gray-500">ID: {specialization.id}</p>
                                                </div>
                                            </div>
                                            <div className="flex space-x-1">
                                                <button className="p-1 text-gray-400 hover:text-blue-600">
                                                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
                                                    </svg>
                                                </button>
                                                <button className="p-1 text-gray-400 hover:text-red-600">
                                                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                                                    </svg>
                                                </button>
                                            </div>
                                        </div>

                                        <div className="mb-4">
                                            <p className="text-sm text-gray-600 line-clamp-3">
                                                {specialization.description || 'No description available'}
                                            </p>
                                        </div>

                                        <div className="flex items-center justify-between pt-4 border-t border-gray-200">
                                            <div className="flex items-center text-sm text-gray-500">
                                                <svg className="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                                                </svg>
                                                <span>Doctors: {specialization.doctorCount || 0}</span>
                                            </div>
                                            <div className="flex space-x-2">
                                                <button className="px-3 py-1 text-xs bg-blue-100 text-blue-700 rounded-full hover:bg-blue-200 transition-colors">
                                                    View Details
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

            {/* Add New Specialization Modal */}
            {showModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white rounded-lg p-6 w-full max-w-md mx-4">
                        <div className="flex justify-between items-center mb-4">
                            <h3 className="text-lg font-semibold text-gray-900">Add New Specialization</h3>
                            <button
                                onClick={handleModalClose}
                                className="text-gray-400 hover:text-gray-600"
                            >
                                ✕
                            </button>
                        </div>

                        <form onSubmit={handleSubmit}>
                            <div className="space-y-4">
                                <div>
                                    <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-1">
                                        Specialization Name *
                                    </label>
                                    <input
                                        type="text"
                                        id="name"
                                        name="name"
                                        value={formData.name}
                                        onChange={handleInputChange}
                                        required
                                        className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                                        placeholder="e.g., Cardiology, Dermatology"
                                        disabled={isSubmitting}
                                    />
                                </div>

                                <div>
                                    <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-1">
                                        Description
                                    </label>
                                    <textarea
                                        id="description"
                                        name="description"
                                        value={formData.description}
                                        onChange={handleInputChange}
                                        rows={3}
                                        className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-orange-500"
                                        placeholder="Brief description of the specialization..."
                                        disabled={isSubmitting}
                                    />
                                </div>
                            </div>

                            <div className="flex justify-end space-x-3 mt-6">
                                <button
                                    type="button"
                                    onClick={handleModalClose}
                                    className="px-4 py-2 text-gray-700 bg-gray-100 rounded-lg hover:bg-gray-200 transition-colors"
                                    disabled={isSubmitting}
                                >
                                    Cancel
                                </button>
                                <button
                                    type="submit"
                                    className="px-4 py-2 bg-orange-600 text-white rounded-lg hover:bg-orange-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                                    disabled={isSubmitting}
                                >
                                    {isSubmitting ? (
                                        <span className="flex items-center">
                                            <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                                            Adding...
                                        </span>
                                    ) : (
                                        'Add'
                                    )}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            <Toaster
                position="top-right"
                toastOptions={{
                    duration: 4000,
                    style: {
                        background: '#fff',
                        color: '#333',
                        boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
                    },
                    success: {
                        iconTheme: {
                            primary: '#10b981',
                            secondary: '#fff',
                        },
                    },
                    error: {
                        iconTheme: {
                            primary: '#ef4444',
                            secondary: '#fff',
                        },
                    },
                }}
            />

            <Footer />
        </div>
    );
};

export default SpecializationsPage;