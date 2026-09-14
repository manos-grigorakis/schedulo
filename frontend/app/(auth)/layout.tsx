import Logo from "@/components/brand/logo";
import MarketingPanel from "./_components/marketing-panel";

export default function AuthLayout({
  children,
}: Readonly<{ children: React.ReactNode }>) {
  return (
    <div>
      <div className="grid grid-cols-1 gap-8 lg:grid-cols-2 min-h-lvh">
        <div className="flex flex-col w-full max-w-xl p-4 mx-auto space-y-4">
          <Logo />

          {children}
        </div>

        <div className="hidden lg:block bg-primary-100 border-l border-[#ccc] p-4">
          <MarketingPanel />
        </div>
      </div>
    </div>
  );
}
