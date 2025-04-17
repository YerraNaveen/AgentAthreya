import React from 'react';
import FileUpload from '@/components/file/FileUpload';
import AuthLayout from '@/components/layout/AuthLayout';

const FileUploadPage: React.FC = () => {
    return (
        <AuthLayout>
            <h1>Upload a File</h1>
            <FileUpload />
        </AuthLayout>
    );
};

export default FileUploadPage;
