import { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';

/**
 * Custom hook for fetching user-specific data
 * @param {string} userType - 'patient', 'doctor', or 'admin'
 * @returns {Object} - { userData, appointments, prescriptions, stats, loading, error, refetch }
 */
export const useUserData = (userType) => {
    const { user } = useAuth();
    const [userData, setUserData] = useState(null);
    const [appointments, setAppointments] = useState([]);
    const [prescriptions, setPrescriptions] = useState([]);
    const [stats, setStats] = useState({});
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchUserData = async () => {
        if (!user?.username) {
            console.log('No user found, skipping data fetch');
            return;
        }

        setLoading(true);
        setError(null);

        try {
            console.log(`Fetching ${userType} data for user:`, user.username);

            // Fetch user profile
            const usersResponse = await fetch(`http://localhost:8080/api/${userType}s`, {
                credentials: 'include',
            });

            if (!usersResponse.ok) {
                throw new Error(`Failed to fetch ${userType}s: ${usersResponse.status}`);
            }

            const allUsers = await usersResponse.json();
            const currentUser = allUsers.find(u =>
                u.username?.toLowerCase() === user.username?.toLowerCase()
            );

            if (!currentUser) {
                throw new Error(`No ${userType} found for username: ${user.username}`);
            }

            console.log(`Current ${userType} found:`, currentUser);
            setUserData(currentUser);

            const userId = currentUser.id;

            // Fetch appointments
            await fetchAppointments(userId, userType);

            // Fetch prescriptions
            await fetchPrescriptions(userId, userType);

        } catch (err) {
            console.error(`Error fetching ${userType} data:`, err);
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const fetchAppointments = async (userId, userType) => {
        try {
            const response = await fetch(`http://localhost:8080/api/appointments/${userType}/${userId}`, {
                credentials: 'include',
            });

            if (response.ok) {
                const data = await response.json();
                console.log(`${userType} appointments:`, data);
                setAppointments(data);
                calculateAppointmentStats(data, userType);
            }
        } catch (err) {
            console.error('Error fetching appointments:', err);
        }
    };

    const fetchPrescriptions = async (userId, userType) => {
        try {
            const response = await fetch(`http://localhost:8080/api/prescriptions/${userType}/${userId}`, {
                credentials: 'include',
            });

            if (response.ok) {
                const data = await response.json();
                console.log(`${userType} prescriptions:`, data);
                setPrescriptions(data);
                calculatePrescriptionStats(data, userType);
            }
        } catch (err) {
            console.error('Error fetching prescriptions:', err);
        }
    };

    const calculateAppointmentStats = (appointmentData, userType) => {
        const today = new Date();
        const todayStart = new Date(today.getFullYear(), today.getMonth(), today.getDate());
        const todayEnd = new Date(todayStart);
        todayEnd.setDate(todayEnd.getDate() + 1);

        const todayCount = appointmentData.filter(apt => {
            const aptDate = new Date(apt.appointmentDate || apt.visitingDate);
            return aptDate >= todayStart && aptDate < todayEnd && apt.status === 'SCHEDULED';
        }).length;

        const upcomingCount = appointmentData.filter(apt => {
            const aptDate = new Date(apt.appointmentDate || apt.visitingDate);
            return aptDate > today && apt.status === 'SCHEDULED';
        }).length;

        const completedCount = appointmentData.filter(apt =>
            apt.status === 'COMPLETED'
        ).length;

        let uniqueCount = 0;
        if (userType === 'doctor') {
            uniqueCount = new Set(appointmentData.map(apt => apt.patientId || apt.patient?.id)).size;
        } else if (userType === 'patient') {
            uniqueCount = new Set(appointmentData.map(apt => apt.doctorId || apt.doctor?.id)).size;
        }

        setStats(prev => ({
            ...prev,
            todayAppointments: todayCount,
            upcomingAppointments: upcomingCount,
            completedVisits: completedCount,
            [userType === 'doctor' ? 'totalPatients' : 'totalDoctors']: uniqueCount
        }));
    };

    const calculatePrescriptionStats = (prescriptionData, userType) => {
        const thirtyDaysAgo = new Date();
        thirtyDaysAgo.setDate(thirtyDaysAgo.getDate() - 30);

        const recentCount = prescriptionData.filter(pres => {
            const presDate = new Date(pres.createdDate || pres.date || pres.prescriptionDate);
            return presDate >= thirtyDaysAgo;
        }).length;

        const activeCount = prescriptionData.filter(pres =>
            pres.status === 'ACTIVE' || pres.isActive
        ).length;

        setStats(prev => ({
            ...prev,
            recentPrescriptions: recentCount,
            activePrescriptions: activeCount
        }));
    };

    useEffect(() => {
        fetchUserData();
    }, [user, userType]);

    return {
        userData,
        appointments,
        prescriptions,
        stats,
        loading,
        error,
        refetch: fetchUserData
    };
};