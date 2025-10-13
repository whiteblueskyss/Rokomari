import { useState, useEffect } from 'react';
import { doctorService, patientService, appointmentService, specializationService, prescriptionService } from '../services/api';

// Generic hook for API data fetching
const useApiData = (apiFunction, dependencies = []) => {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchData = async () => {
        try {
            setLoading(true);
            setError(null);
            const result = await apiFunction();
            setData(result);
        } catch (err) {
            setError(err.message);
            console.error('Error fetching data:', err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchData();
    }, dependencies);

    return { data, loading, error, refetch: fetchData };
};

// Hook for doctors data
export const useDoctors = () => {
    return useApiData(doctorService.getAllDoctors);
};

// Hook for patients data
export const usePatients = () => {
    return useApiData(patientService.getAllPatients);
};

// Hook for appointments data
export const useAppointments = () => {
    return useApiData(appointmentService.getAllAppointments);
};

// Hook for specializations data
export const useSpecializations = () => {
    return useApiData(specializationService.getAllSpecializations);
};

// Hook for single doctor
export const useDoctor = (id) => {
    return useApiData(() => doctorService.getDoctorById(id), [id]);
};

// Hook for single patient
export const usePatient = (id) => {
    return useApiData(() => patientService.getPatientById(id), [id]);
};

// Hook for single appointment
export const useAppointment = (id) => {
    return useApiData(() => appointmentService.getAppointmentById(id), [id]);
};

// Hook for single specialization
export const useSpecialization = (id) => {
    return useApiData(() => specializationService.getSpecializationById(id), [id]);
};

// Hook for prescriptions data
export const usePrescriptions = () => {
    return useApiData(prescriptionService.getAllPrescriptions);
};

// Hook for prescriptions by patient ID
export const usePrescriptionsByPatient = (patientId) => {
    return useApiData(() => prescriptionService.getPrescriptionsByPatientId(patientId), [patientId]);
};

// Hook for prescriptions by doctor ID
export const usePrescriptionsByDoctor = (doctorId) => {
    return useApiData(() => prescriptionService.getPrescriptionsByDoctorId(doctorId), [doctorId]);
};

// Hook for single prescription
export const usePrescription = (id) => {
    return useApiData(() => prescriptionService.getPrescriptionById(id), [id]);
};