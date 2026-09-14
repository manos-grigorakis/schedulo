import Divider from "@/components/ui/divider";

export default function MarketingPanel() {
  const stats: { counter: string; description: string }[] = [
    { counter: "200+", description: "shops booked out" },
    { counter: "2min", description: "average setup" },
    { counter: "0", description: "phone calls during a fade" },
  ];

  return (
    <div className="mt-50">
      <p className="mb-10 font-serif text-4xl italic leading-tight text-primary-900">
        &quot;Two chairs, one phone. <br /> Now nobody calls during a fade, they
        just book.&quot;
      </p>

      <Divider />

      <div className="grid items-center grid-cols-3 gap-8 mt-10 text-center">
        {stats.map((stat) => (
          <div key={stat.description}>
            <span className="text-3xl font-bold text-primary-900">
              {stat.counter}
            </span>
            <p className="mt-1 text-sm text-primary-700">{stat.description}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
