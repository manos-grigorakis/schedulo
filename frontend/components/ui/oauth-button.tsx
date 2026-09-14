import Image from "next/image";

type OAuthButtonProps = {
  provider: string;
  icon: string;
};

export default function OAuthButton({ provider, icon }: OAuthButtonProps) {
  return (
    <button className="flex items-center justify-center w-full gap-3 px-4 py-3 text-sm font-medium transition-colors bg-white border cursor-pointer rounded-xl border-neutral-900/10 text-neutral-900 hover:bg-neutral-50">
      <Image src={icon} width={23} height={23} alt="" />
      Continue with {provider}
    </button>
  );
}
