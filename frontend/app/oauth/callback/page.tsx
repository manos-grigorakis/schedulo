"use client";

import { useRouter } from "next/navigation";
import { useEffect } from "react";

export default function OAuthCallback() {
  const router = useRouter();

  useEffect(() => {
    router.replace("/dashboard");
  }, [router]);

  return <p className="text-center">Signing you in...</p>;
}
