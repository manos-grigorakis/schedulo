import Image from "next/image";

interface OAuthButtonProps {
  provider: string;
  icon: string;
}

export default function OAuthButton({ provider, icon }: OAuthButtonProps) {
  const handleOAuth = () => {
    window.location.href = `${process.env.NEXT_PUBLIC_BACKEND_URL}/oauth2/authorization/${provider.toLocaleLowerCase()}`;
  };

  return (
    <button
      onClick={handleOAuth}
      className="flex items-center justify-center w-full gap-3 px-4 py-3 text-sm font-medium transition-colors bg-white border cursor-pointer rounded-lg border-neutral-200  hover:bg-neutral-100"
    >
      <Image src={icon} width={23} height={23} alt="" />
      Continue with {provider}
    </button>
  );
}
