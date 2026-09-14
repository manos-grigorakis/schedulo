import Divider from "@/components/ui/divider";
import OAuthButton from "@/components/ui/oauth-button";

type AuthContentProps = { title: string; subtitle: string };

export default function AuthContent({ title, subtitle }: AuthContentProps) {
  return (
    <div className="flex flex-col gap-4 mb-6">
      <div className="mt-10">
        <h2 className="mb-1 text-2xl font-semibold">{title}</h2>
        <span className="text-muted">{subtitle}</span>
      </div>

      <OAuthButton provider="Google" icon="/icons/google.svg" />
      <OAuthButton provider="Microsoft" icon="/icons/microsoft.svg" />

      <div className="flex items-center gap-4">
        <Divider />
        <span className="text-sm text-center text-muted">or</span>
        <Divider />
      </div>
    </div>
  );
}
