
import React, { ReactNode } from 'react';
import { useAuth } from '@/context/AuthContext';
import { Button } from '@/components/ui/button';
import { LogOut, User } from 'lucide-react';

interface DashboardLayoutProps {
  children: ReactNode;
}

const DashboardLayout: React.FC<DashboardLayoutProps> = ({ children }) => {
  const { user, logout } = useAuth();

  return (
    <div className="flex flex-col h-screen bg-background">
      <header className="border-b bg-card py-3 px-4 shadow-sm">
        <div className="container mx-auto flex justify-between items-center">
          <h1 className="text-xl font-bold text-primary">Communique</h1>
          
          <div className="flex items-center gap-4">
            <div className="flex items-center">
              <div className="h-8 w-8 rounded-full bg-primary flex items-center justify-center mr-2">
                <User className="h-4 w-4 text-primary-foreground" />
              </div>
              <span className="font-medium">{user?.name || 'User'}</span>
            </div>
            
            <Button variant="ghost" size="sm" onClick={logout}>
              <LogOut className="h-4 w-4 mr-2" />
              Sign out
            </Button>
          </div>
        </div>
      </header>
      
      <main className="flex-1 container mx-auto px-4 py-6 md:px-6 lg:px-8 overflow-hidden">
        {children}
      </main>
    </div>
  );
};

export default DashboardLayout;
