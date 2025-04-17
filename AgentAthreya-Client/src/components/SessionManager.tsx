import React, { useEffect, useState } from 'react';

interface SessionManagerProps {
    sessionDuration: number; // Session duration in seconds
    onExtendSession: () => Promise<void>; // Callback to handle session extension
    toast: (options: { title: string; description: string; variant?: string }) => void; // Toast notification function
}

const SessionManager: React.FC<SessionManagerProps> = ({ sessionDuration, onExtendSession, toast }) => {
    const [showPopup, setShowPopup] = useState<boolean>(false);

    useEffect(() => {
        // Calculate the time to show the popup (5 minutes 30 seconds)
        const popupTime = (sessionDuration - 30) * 1000;

        const timer = setTimeout(() => {
            setShowPopup(true);
        }, popupTime);

        return () => clearTimeout(timer);
    }, [sessionDuration]);

    const handleExtend = async () => {
        setShowPopup(false);
        try {
            await onExtendSession();
            toast({
                title: "Session Extended",
                description: "Your session has been successfully extended.",
            });
        } catch (error) {
            toast({
                title: "Session Extension Failed",
                description: "Unable to extend your session. Please log in again.",
                variant: "destructive",
            });
        }
    };

    const handleClose = () => {
        setShowPopup(false);
    };

    return (
        <div>
            {showPopup && (
                <div className="popup">
                    <p>Your session is about to end. Do you want to extend it?</p>
                    <button onClick={handleExtend}>Yes</button>
                    <button onClick={handleClose}>No</button>
                </div>
            )}
        </div>
    );
};

export default SessionManager;