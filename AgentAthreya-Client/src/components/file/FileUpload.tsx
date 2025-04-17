import React, { useState } from 'react';
import fileService from '@/services/fileService';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';

const FileUpload: React.FC = () => {
    const [file, setFile] = useState<File | null>(null);
    const [uploading, setUploading] = useState(false);
    const [message, setMessage] = useState('');

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files) {
            setFile(e.target.files[0]);
        }
    };

    const handleUpload = async () => {
        if (!file) return;

        setUploading(true);
        setMessage('');

        const result = await fileService.uploadFile(file);

        if (result.success) {
            setMessage('File uploaded successfully!');
        } else {
            setMessage(`Failed to upload file: ${result.error}`);
        }

        setUploading(false);
    };

    return (
        <div className="file-upload">
            <Input type="file" accept=".pdf,.doc,.docx" onChange={handleFileChange} />
            <Button onClick={handleUpload} disabled={uploading || !file}>
                {uploading ? 'Uploading...' : 'Upload'}
            </Button>
            {message && <p>{message}</p>}
        </div>
    );
};

export default FileUpload;
