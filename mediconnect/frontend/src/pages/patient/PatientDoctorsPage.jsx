import React, { useState } from 'react';
import { useDoctors } from '../../hooks/useApi';
import { useAuth } from '../../contexts/AuthContext';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { LoadingSpinner, ErrorMessage } from '../../components/common';
import toast from 'react-hot-toast';

const PatientDoctorsPage = () => {
    const { data: doctors, loading, error, refetch } = useDoctors();
    const { user } = useAuth();
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedSpecialization, setSelectedSpecialization] = useState('');

    // Modal state
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedDoctor, setSelectedDoctor] = useState(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    // Form data
    const [formData, setFormData] = useState({
        visitingDate: '',
        problemDescription: ''
    });

    // Get unique specializations for filter
    const specializations = [...new Set(doctors.map(doctor => doctor.specializations).filter(Boolean))];

    // Generate next 30 days for date selection
    const generateAvailableDates = () => {
        const dates = [];
        const today = new Date();
        for (let i = 1; i <= 30; i++) {
            const date = new Date(today);
            date.setDate(today.getDate() + i);
            dates.push(date.toISOString().split('T')[0]);
        }
        return dates;
    };

    const availableDates = generateAvailableDates();

    // Handle modal functions
    const openModal = (doctor) => {
        setSelectedDoctor(doctor);
        setFormData({
            visitingDate: '',
            problemDescription: ''
        });
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
        setSelectedDoctor(null);
        setFormData({
            visitingDate: '',
            problemDescription: ''
        });
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!formData.visitingDate || !formData.problemDescription.trim()) {
            toast.error('Please fill in all fields');
            return;
        }

        if (!user?.id) {
            toast.error('User not authenticated');
            return;
        }

        setIsSubmitting(true);

        try {
            const appointmentData = {
                doctorId: selectedDoctor.id,
                patientId: user.id,
                visitingDate: formData.visitingDate,
                problemDescription: formData.problemDescription.trim()
            };

            const response = await fetch('http://localhost:8080/api/appointments', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
                body: JSON.stringify(appointmentData)
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Failed to create appointment');
            }

            const result = await response.json();
            toast.success('Appointment booked successfully!');
            closeModal();
        } catch (error) {
            console.error('Error creating appointment:', error);
            toast.error(error.message || 'Failed to book appointment');
        } finally {
            setIsSubmitting(false);
        }
    };

    // Filter doctors based on search term and specialization
    const filteredDoctors = doctors.filter(doctor => {
        const matchesSearch = doctor.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
            doctor.email?.toLowerCase().includes(searchTerm.toLowerCase()) ||
            doctor.specializations?.toLowerCase().includes(searchTerm.toLowerCase());
        const matchesSpecialization = selectedSpecialization === '' ||
            doctor.specializations?.toLowerCase() === selectedSpecialization.toLowerCase();
        return matchesSearch && matchesSpecialization;
    });

    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex flex-col">
                <Header title="Find Doctors" showUserInfo={true} showLogout={true} />
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
                <Header title="Find Doctors" showUserInfo={true} showLogout={true} />
                <div className="flex-1 flex items-center justify-center">
                    <ErrorMessage message={error} />
                </div>
                <Footer />
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <Header title="Find Doctors" showUserInfo={true} showLogout={true} />

            <main className="flex-1 container mx-auto px-4 py-8 max-w-7xl">
                <div className="space-y-6">
                    {/* Header */}
                    <div className="flex justify-between items-center">
                        <div>
                            <h2 className="text-2xl font-bold text-gray-900">Find Doctors</h2>
                            <p className="text-gray-600">Browse available doctors and their specializations</p>
                        </div>
                    </div>

                    {/* Search and Filter */}
                    <div className="bg-white p-6 rounded-lg shadow-sm border">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-2">Search Doctors</label>
                                <input
                                    type="text"
                                    placeholder="Search by name, email, or specialization..."
                                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent"
                                    value={searchTerm}
                                    onChange={(e) => setSearchTerm(e.target.value)}
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-2">Filter by Specialization</label>
                                <select
                                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent"
                                    value={selectedSpecialization}
                                    onChange={(e) => setSelectedSpecialization(e.target.value)}
                                >
                                    <option value="">All Specializations</option>
                                    {specializations.map((spec, index) => (
                                        <option key={index} value={spec}>{spec}</option>
                                    ))}
                                </select>
                            </div>
                        </div>
                    </div>

                    {/* Results Count */}
                    <div className="text-sm text-gray-600">
                        Showing {filteredDoctors.length} of {doctors.length} doctors
                    </div>

                    {/* Doctors Grid */}
                    {filteredDoctors.length === 0 ? (
                        <div className="text-center py-12">
                            <div className="text-gray-400 mb-4">
                                <svg className="w-16 h-16 mx-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                                </svg>
                            </div>
                            <p className="text-gray-500 mb-4">No doctors found matching your criteria</p>
                            <button
                                onClick={() => {
                                    setSearchTerm('');
                                    setSelectedSpecialization('');
                                }}
                                className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700"
                            >
                                Clear Filters
                            </button>
                        </div>
                    ) : (
                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                            {filteredDoctors.map((doctor) => (
                                <div key={doctor.id} className="bg-white p-6 rounded-lg shadow-sm border hover:shadow-md transition-shadow">
                                    <div className="flex items-center mb-4">
                                        <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center">
                                            <span className="text-green-600 font-semibold text-lg">
                                                {doctor.name ? doctor.name.charAt(0).toUpperCase() : 'D'}
                                            </span>
                                        </div>
                                        <div className="ml-4">
                                            <h3 className="text-lg font-semibold text-gray-900">
                                                Dr. {doctor.name || 'Unknown'}
                                            </h3>
                                            <p className="text-sm text-gray-600">ID: {doctor.id}</p>
                                        </div>
                                    </div>

                                    <div className="space-y-2">
                                        <div>
                                            <span className="text-sm font-medium text-gray-700">Specialization:</span>
                                            <p className="text-sm text-gray-600">{doctor.specializations || 'Not specified'}</p>
                                        </div>
                                        <div>
                                            <span className="text-sm font-medium text-gray-700">Email:</span>
                                            <p className="text-sm text-gray-600">{doctor.email || 'Not provided'}</p>
                                        </div>
                                        <div>
                                            <span className="text-sm font-medium text-gray-700">Phone:</span>
                                            <p className="text-sm text-gray-600">{doctor.phone || 'Not provided'}</p>
                                        </div>
                                    </div>

                                    <div className="mt-4 pt-4 border-t">
                                        <button
                                            onClick={() => openModal(doctor)}
                                            className="w-full px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
                                        >
                                            Book Appointment
                                        </button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </main>

            {/* Appointment Booking Modal */}
            {isModalOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
                    <div className="bg-white rounded-lg max-w-md w-full max-h-[90vh] overflow-y-auto">
                        <div className="p-6">
                            <div className="flex justify-between items-center mb-6">
                                <h2 className="text-xl font-bold text-gray-900">Book Appointment</h2>
                                <button
                                    onClick={closeModal}
                                    className="text-gray-400 hover:text-gray-600"
                                    disabled={isSubmitting}
                                >
                                    <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                                    </svg>
                                </button>
                            </div>

                            {selectedDoctor && (
                                <div className="mb-6 p-4 bg-gray-50 rounded-lg">
                                    <h3 className="font-semibold text-gray-900">Doctor Information</h3>
                                    <p className="text-sm text-gray-600">Dr. {selectedDoctor.name}</p>
                                    <p className="text-sm text-gray-600">{selectedDoctor.specializations}</p>
                                    <p className="text-sm text-gray-600">ID: {selectedDoctor.id}</p>
                                </div>
                            )}

                            <form onSubmit={handleSubmit} className="space-y-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-2">
                                        Patient ID
                                    </label>
                                    <input
                                        type="text"
                                        value={user?.id || ''}
                                        disabled
                                        className="w-full px-3 py-2 border border-gray-300 rounded-lg bg-gray-100 text-gray-500 cursor-not-allowed"
                                    />
                                </div>

                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-2">
                                        Doctor ID
                                    </label>
                                    <input
                                        type="text"
                                        value={selectedDoctor?.id || ''}
                                        disabled
                                        className="w-full px-3 py-2 border border-gray-300 rounded-lg bg-gray-100 text-gray-500 cursor-not-allowed"
                                    />
                                </div>

                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-2">
                                        Visiting Date *
                                    </label>
                                    <select
                                        name="visitingDate"
                                        value={formData.visitingDate}
                                        onChange={handleInputChange}
                                        required
                                        disabled={isSubmitting}
                                        className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent disabled:bg-gray-100 disabled:cursor-not-allowed"
                                    >
                                        <option value="">Select a date</option>
                                        {availableDates.map((date) => (
                                            <option key={date} value={date}>
                                                {new Date(date).toLocaleDateString('en-US', {
                                                    weekday: 'long',
                                                    year: 'numeric',
                                                    month: 'long',
                                                    day: 'numeric'
                                                })}
                                            </option>
                                        ))}
                                    </select>
                                </div>

                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-2">
                                        Problem Description *
                                    </label>
                                    <textarea
                                        name="problemDescription"
                                        value={formData.problemDescription}
                                        onChange={handleInputChange}
                                        placeholder="Describe your symptoms or health concerns..."
                                        required
                                        disabled={isSubmitting}
                                        rows="4"
                                        className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent disabled:bg-gray-100 disabled:cursor-not-allowed resize-none"
                                    />
                                </div>

                                <div className="flex gap-3 pt-4">
                                    <button
                                        type="button"
                                        onClick={closeModal}
                                        disabled={isSubmitting}
                                        className="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                                    >
                                        Cancel
                                    </button>
                                    <button
                                        type="submit"
                                        disabled={isSubmitting}
                                        className="flex-1 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center"
                                    >
                                        {isSubmitting ? (
                                            <>
                                                <svg className="animate-spin -ml-1 mr-2 h-4 w-4 text-white" fill="none" viewBox="0 0 24 24">
                                                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                                                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                                                </svg>
                                                Booking...
                                            </>
                                        ) : (
                                            'Book Appointment'
                                        )}
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            )}

            <Footer />
        </div>
    );
};

export default PatientDoctorsPage;