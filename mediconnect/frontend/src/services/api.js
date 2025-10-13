const API_BASE_URL = 'http://localhost:8080/api';

// Generic API request function
const apiRequest = async (endpoint, options = {}) => {
    const url = `${API_BASE_URL}${endpoint}`;
    const config = {
        credentials: 'include', // Include cookies for authentication
        headers: {
            'Content-Type': 'application/json',
            ...options.headers,
        },
        ...options,
    };

    try {
        const response = await fetch(url, config);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('API request failed:', error);
        throw error;
    }
};

// Doctor API functions
export const doctorService = {
    // Get all doctors
    getAllDoctors: () => apiRequest('/doctors'),

    // Get doctor by ID
    getDoctorById: (id) => apiRequest(`/doctors/${id}`),

    // Create new doctor
    createDoctor: (doctor) => apiRequest('/doctors', {
        method: 'POST',
        body: JSON.stringify(doctor),
    }),

    // Update doctor
    updateDoctor: (id, doctor) => apiRequest(`/doctors/${id}`, {
        method: 'PUT',
        body: JSON.stringify(doctor),
    }),

    // Delete doctor
    deleteDoctor: (id) => apiRequest(`/doctors/${id}`, {
        method: 'DELETE',
    }),
};

// Patient API functions
export const patientService = {
    // Get all patients
    getAllPatients: () => apiRequest('/patients'),

    // Get patient by ID
    getPatientById: (id) => apiRequest(`/patients/${id}`),

    // Create new patient
    createPatient: (patient) => apiRequest('/patients', {
        method: 'POST',
        body: JSON.stringify(patient),
    }),

    // Update patient
    updatePatient: (id, patient) => apiRequest(`/patients/${id}`, {
        method: 'PUT',
        body: JSON.stringify(patient),
    }),

    // Delete patient
    deletePatient: (id) => apiRequest(`/patients/${id}`, {
        method: 'DELETE',
    }),
};

// Appointment API functions
export const appointmentService = {
    // Get all appointments
    getAllAppointments: () => apiRequest('/appointments'),

    // Get appointment by ID
    getAppointmentById: (id) => apiRequest(`/appointments/${id}`),

    // Create new appointment
    createAppointment: (appointment) => apiRequest('/appointments', {
        method: 'POST',
        body: JSON.stringify(appointment),
    }),

    // Update appointment
    updateAppointment: (id, appointment) => apiRequest(`/appointments/${id}`, {
        method: 'PUT',
        body: JSON.stringify(appointment),
    }),

    // Delete appointment
    deleteAppointment: (id) => apiRequest(`/appointments/${id}`, {
        method: 'DELETE',
    }),
};

// Specialization API functions
export const specializationService = {
    // Get all specializations
    getAllSpecializations: () => apiRequest('/specializations'),

    // Get specialization by ID
    getSpecializationById: (id) => apiRequest(`/specializations/${id}`),

    // Create new specialization
    createSpecialization: (specialization) => apiRequest('/specializations', {
        method: 'POST',
        body: JSON.stringify(specialization),
    }),

    // Update specialization
    updateSpecialization: (id, specialization) => apiRequest(`/specializations/${id}`, {
        method: 'PUT',
        body: JSON.stringify(specialization),
    }),

    // Delete specialization
    deleteSpecialization: (id) => apiRequest(`/specializations/${id}`, {
        method: 'DELETE',
    }),
};

// Prescription API functions
export const prescriptionService = {
    // Get all prescriptions
    getAllPrescriptions: () => apiRequest('/prescriptions'),

    // Get prescription by ID
    getPrescriptionById: (id) => apiRequest(`/prescriptions/${id}`),

    // Get prescriptions by patient ID
    getPrescriptionsByPatientId: (patientId) => apiRequest(`/prescriptions/patient/${patientId}`),

    // Get prescriptions by doctor ID
    getPrescriptionsByDoctorId: (doctorId) => apiRequest(`/prescriptions/doctor/${doctorId}`),

    // Create new prescription
    createPrescription: (prescription) => apiRequest('/prescriptions', {
        method: 'POST',
        body: JSON.stringify(prescription),
    }),

    // Update prescription
    updatePrescription: (id, prescription) => apiRequest(`/prescriptions/${id}`, {
        method: 'PUT',
        body: JSON.stringify(prescription),
    }),

    // Delete prescription
    deletePrescription: (id) => apiRequest(`/prescriptions/${id}`, {
        method: 'DELETE',
    }),
};